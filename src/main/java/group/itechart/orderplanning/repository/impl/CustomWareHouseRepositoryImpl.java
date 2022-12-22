package group.itechart.orderplanning.repository.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import group.itechart.orderplanning.repository.CustomWareHouseRepository;
import group.itechart.orderplanning.repository.entity.WareHouse;
import group.itechart.orderplanning.repository.entity.WareHouse_;


@Repository
public class CustomWareHouseRepositoryImpl implements CustomWareHouseRepository {

	private final EntityManager em;

	public CustomWareHouseRepositoryImpl(final EntityManager em) {
		this.em = em;
	}

	@Override
	public List<WareHouse> findWareHousesByGeoHash(final List<String> geoHashes) {
		if (geoHashes.isEmpty()) {
			return Collections.emptyList();
		}

		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<WareHouse> cq = cb.createQuery(WareHouse.class);
		final Root<WareHouse> root = cq.from(WareHouse.class);
		final int geoHashLength = geoHashes.get(0).length();
		ParameterExpression<Integer> param1 = cb.parameter(Integer.class);
		cq.where(cb.function("LEFT", String.class, root.get(WareHouse_.geoHash), param1).in(geoHashes));
		final TypedQuery<WareHouse> query = em.createQuery(cq);
		query.setParameter(param1, geoHashLength);
		return query.getResultList();
	}
}
