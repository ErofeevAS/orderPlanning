package group.itechart.orderplanning.strategy.impl;

import ch.hsr.geohash.GeoHash;
import group.itechart.orderplanning.repository.ClientRepository;
import group.itechart.orderplanning.repository.entity.*;
import group.itechart.orderplanning.service.ProductService;
import group.itechart.orderplanning.service.WareHouseService;
import group.itechart.orderplanning.service.dto.OrderDto;
import group.itechart.orderplanning.strategy.DeliveryStrategy;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

import static group.itechart.orderplanning.utils.GeoHashUtils.calculateDistance;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class DefaultDeliveryStrategy implements DeliveryStrategy {

    private final ProductService productService;
    private final WareHouseService wareHouseService;
    private final ClientRepository clientRepository;
    private final static int GEO_HASH_ACCURACY = 6;

    public DefaultDeliveryStrategy(ProductService productService, WareHouseService wareHouseService,
            final ClientRepository clientRepository) {
               this.productService = productService;
        this.wareHouseService = wareHouseService;
        this.clientRepository = clientRepository;
    }

    @Override
    public Order delivery(Order order) {
        populateOrderEntries(order);
        return order;
    }

    @Override
    public DeliveryStrategyName getStrategyName() {
        return DeliveryStrategyName.DEFAULT;
    }

    private void populateOrderEntries(Order order) {
        final List<OrderEntry> orderEntries = order.getOrderEntries();
        Client client = order.getClient();
        if (orderEntries.isEmpty() || isNull(client)) {
            return;
        }

        client = getClient(client.getId());
        final Coordinates clientCoordinates = client.getCity().getCoordinates();
        final String clientGeoHash = getClientGeoHash(clientCoordinates);

        for (OrderEntry orderEntry : orderEntries) {
            final Long productId = orderEntry.getProduct().getId();
            final Product product = productService.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("product not found, id" + productId));
            final int productAmount = orderEntry.getAmount();
            final List<WareHouse> wareHouses = wareHouseService.findWareHousesInCoordinates(clientCoordinates, productId, productAmount);
            final WareHouse nearestWareHouse = findNearestWareHouse(wareHouses, clientGeoHash);

            if (nonNull(nearestWareHouse)) {
                final double distance = calculateDistance(nearestWareHouse.getGeoHash(), clientGeoHash);
                orderEntry.setAmount(productAmount);
                orderEntry.setProduct(product);
                orderEntry.setWareHouse(nearestWareHouse);
                orderEntry.setDistance(distance);
            }
        }
    }

    protected WareHouse findNearestWareHouse(final List<WareHouse> wareHouses, final String clientGeoHash) {
        if (wareHouses.size() == 1) {
            return wareHouses.get(0);
        }
        double minDistance = Double.MAX_VALUE;
        WareHouse nearestWareHouse = null;

        for (WareHouse wareHouse : wareHouses) {
            final double distance = calculateDistance(wareHouse.getGeoHash(), clientGeoHash);
            if (distance < minDistance) {
                minDistance = distance;
                nearestWareHouse = wareHouse;
            }
        }
        return nearestWareHouse;
    }

    protected String getClientGeoHash(final Coordinates clientCoordinates) {
        return GeoHash.geoHashStringWithCharacterPrecision(clientCoordinates.getLatitude(),
                clientCoordinates.getLongitude(), GEO_HASH_ACCURACY);
    }

    private Client getClient(final Long  clientId) {
        final Optional<Client> optionalClient = clientRepository.findById(clientId);
        return optionalClient.orElseThrow(() -> new EntityNotFoundException("client not found, id: " + clientId));
    }
}
