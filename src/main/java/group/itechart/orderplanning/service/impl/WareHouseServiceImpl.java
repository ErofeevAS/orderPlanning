package group.itechart.orderplanning.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import group.itechart.orderplanning.repository.WareHouseRepository;
import group.itechart.orderplanning.repository.entity.WareHouse;
import group.itechart.orderplanning.service.WareHouseService;


@Service
public class WareHouseServiceImpl implements WareHouseService {

	private final WareHouseRepository wareHouseRepository;

	public WareHouseServiceImpl(final WareHouseRepository wareHouseRepository) {
		this.wareHouseRepository = wareHouseRepository;
	}

	@Override
	public List<WareHouse> findByProductId(final Long productId) {
		return wareHouseRepository.findWareHousesByProduct(productId);
	}

	@Override
	public List<WareHouse> findByGeoHash(final List<String> geoHash) {
		return wareHouseRepository.findWareHousesByGeoHash(geoHash);
	}

}
