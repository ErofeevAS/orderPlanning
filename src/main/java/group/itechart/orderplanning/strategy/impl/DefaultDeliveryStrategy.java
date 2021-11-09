package group.itechart.orderplanning.strategy.impl;

import ch.hsr.geohash.GeoHash;
import group.itechart.orderplanning.repository.ProductRepository;
import group.itechart.orderplanning.repository.entity.*;
import group.itechart.orderplanning.service.WareHouseService;
import group.itechart.orderplanning.strategy.DeliveryStrategy;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

import static group.itechart.orderplanning.utils.GeoHashUtils.calculateDistance;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class DefaultDeliveryStrategy implements DeliveryStrategy {

    private final ProductRepository productRepository;
    private final WareHouseService wareHouseService;
    private final static int GEO_HASH_ACCURACY = 6;

    public DefaultDeliveryStrategy(ProductRepository productRepository, WareHouseService wareHouseService) {
               this.productRepository = productRepository;
        this.wareHouseService = wareHouseService;
    }

    @Override
    public Order delivery(Order order) {
        populateOrderEntries(order.getClient(), order.getOrderEntries());
        return order;
    }

    @Override
    public DeliveryStrategyName getStrategyName() {
        return DeliveryStrategyName.DEFAULT;
    }

    private List<OrderEntry> populateOrderEntries(final Client client, List<OrderEntry> orderEntries) {
        if (orderEntries.isEmpty() || isNull(client)) {
            return Collections.emptyList();
        }

        final Coordinates clientCoordinates = client.getCity().getCoordinates();
        final String clientGeoHash = GeoHash.geoHashStringWithCharacterPrecision(clientCoordinates.getLatitude(),
                clientCoordinates.getLongitude(), GEO_HASH_ACCURACY);

        for (OrderEntry orderEntry : orderEntries) {
            final Long productId = orderEntry.getProduct().getId();
            final Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("product not found, id" + productId));
            final int productAmount = orderEntry.getAmount();
            final List<WareHouse> wareHouses = wareHouseService.getWareHousesInCoordinates(clientCoordinates, productId, productAmount);
            final WareHouse nearestWareHouse = findNearestWareHouse(wareHouses, clientGeoHash);

            if (nonNull(nearestWareHouse)) {
                final double distance = calculateDistance(nearestWareHouse.getGeoHash(), clientGeoHash);
                orderEntry.setAmount(productAmount);
                orderEntry.setProduct(product);
                orderEntry.setWareHouse(nearestWareHouse);
                orderEntry.setDistance(distance);
            }
        }
        return orderEntries;
    }


    private WareHouse findNearestWareHouse(final List<WareHouse> wareHouses, final String clientGeoHash) {
        if (wareHouses.size() == 1) {
            return wareHouses.get(0);
        }
        double minDistance = Double.MAX_VALUE;
        WareHouse nearestWareHouse = null;

        for (WareHouse wareHouse : wareHouses) {
            final double distance = calculateDistance(wareHouse.getGeoHash(), clientGeoHash);
            if (distance < minDistance) {
                nearestWareHouse = wareHouse;
            }
        }
        return nearestWareHouse;
    }
}
