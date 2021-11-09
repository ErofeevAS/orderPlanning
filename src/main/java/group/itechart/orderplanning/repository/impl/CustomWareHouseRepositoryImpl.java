package group.itechart.orderplanning.repository.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import group.itechart.orderplanning.repository.CustomWareHouseRepository;
import group.itechart.orderplanning.repository.entity.WareHouse;


@Repository
public class CustomWareHouseRepositoryImpl implements CustomWareHouseRepository {

	private final EntityManager em;
	private final static String columnName = "geoHash";

	public CustomWareHouseRepositoryImpl(final EntityManager em) {
		this.em = em;
	}

	@Override
	public List<WareHouse> findWareHousesByGeoHash(final List<String> geoHashes) {
		if (geoHashes.isEmpty()) {
			return Collections.emptyList();
		}
		final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		final CriteriaQuery<WareHouse> criteriaQuery = criteriaBuilder.createQuery(WareHouse.class);
		final Root<WareHouse> wareHouse = criteriaQuery.from(WareHouse.class);
		final int geoHashLength = geoHashes.get(0).length();
		if (geoHashLength >= 2 && geoHashLength <= 6) {
			criteriaQuery.select(wareHouse).where(wareHouse.get(columnName + geoHashLength).in(geoHashes));
		}
		else {
			criteriaQuery.select(wareHouse).where(wareHouse.get(columnName).in(geoHashes));
		}
		final TypedQuery<WareHouse> query = em.createQuery(criteriaQuery);
		return query.getResultList();
	}
}
