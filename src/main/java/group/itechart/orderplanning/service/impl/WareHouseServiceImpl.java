package group.itechart.orderplanning.service.impl;

import static group.itechart.orderplanning.utils.GeoHashUtils.getCommonGeoHashForCircle;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import group.itechart.orderplanning.repository.WareHouseRepository;
import group.itechart.orderplanning.repository.entity.Coordinates;
import group.itechart.orderplanning.repository.entity.StockLevel;
import group.itechart.orderplanning.repository.entity.WareHouse;
import group.itechart.orderplanning.service.WareHouseService;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class WareHouseServiceImpl implements WareHouseService {

	private final static int GEO_HASH_ACCURACY = 6;
	private final static double[] radiuses = { 0.5, 6, 20, 80, 600, 2500 };

	private final WareHouseRepository wareHouseRepository;

	public WareHouseServiceImpl(final WareHouseRepository wareHouseRepository) {
		this.wareHouseRepository = wareHouseRepository;
	}

	@Override
	public List<WareHouse> findByProductId(final Long productId) {
		return wareHouseRepository.findWareHousesByProduct(productId);
	}

	@Override
	public List<WareHouse> findWareHousesInRadius(final Coordinates clientCoordinates, final double radius, int accuracy) {
		final List<String> geoHashes = getCommonGeoHashForCircle(clientCoordinates.getLatitude(),
				clientCoordinates.getLongitude(), radius, accuracy);
		return wareHouseRepository.findWareHousesByGeoHash(geoHashes);
	}

	@Override
	public List<WareHouse> getWareHousesInCoordinates(final Coordinates clientCoordinates, final Long productId,
			final int productAmount) {

		List<WareHouse> wareHouses;
		int accuracy = GEO_HASH_ACCURACY;
		for (double radius : radiuses) {
			log.debug("------SEARCHING WAREHOUSES IN {} km radius for product with id: {}", radius, productId);
			wareHouses = findWareHousesInRadius(clientCoordinates, radius, accuracy);
			log.debug("------were found {} warehouses for product with id: {}", wareHouses.size(), productId);
			wareHouses = filterWareHousesByProductAvailability(productId, productAmount, wareHouses);
			if (!wareHouses.isEmpty()) {
				return wareHouses;
			}
			accuracy--;
		}
		String message = MessageFormat.format("product id: {0} and amount: {1} is out of stocks", productId, productAmount);
		throw new EntityNotFoundException(message);
	}

	private List<WareHouse> filterWareHousesByProductAvailability(final Long productId, final int productAmount, final List<WareHouse> wareHouses) {
		return wareHouses.stream()
				.map(WareHouse::getStockLevels)
				.flatMap(Collection::stream)
				.filter(sl -> sl.getProduct().getId().equals(productId))
				.filter(sl -> sl.getAmount() >= productAmount)
				.map(StockLevel::getWareHouse).collect(Collectors.toList());
	}

}
