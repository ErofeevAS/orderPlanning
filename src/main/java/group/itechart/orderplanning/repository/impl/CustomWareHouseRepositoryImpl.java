package group.itechart.orderplanning.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import group.itechart.orderplanning.repository.CustomWareHouseRepository;
import group.itechart.orderplanning.repository.entity.StockLevel_;
import group.itechart.orderplanning.repository.entity.WareHouse;
import group.itechart.orderplanning.repository.entity.WareHouse_;


@Repository
public class CustomWareHouseRepositoryImpl implements CustomWareHouseRepository {

	private final EntityManager em;

	public CustomWareHouseRepositoryImpl(final EntityManager em) {
		this.em = em;
	}

	@Override
	public List<WareHouse> findWareHousesByGeoHash(final List<String> geoHashes, final Long productId, final int amount) {
		if (geoHashes.isEmpty() || productId == null) {
			return Collections.emptyList();
		}

		final List<Predicate> predicates = new ArrayList<>();

		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<WareHouse> cq = cb.createQuery(WareHouse.class);
		final Root<WareHouse> root = cq.from(WareHouse.class);
		Join<Object, Object> stocklevels = root.join(WareHouse_.STOCK_LEVELS);

		predicates.add(cb.equal(stocklevels.get(StockLevel_.PRODUCT), productId));
		predicates.add(cb.ge(stocklevels.get(StockLevel_.AMOUNT), amount));
		final int geoHashLength = geoHashes.get(0).length();
		ParameterExpression<Integer> param1 = cb.parameter(Integer.class);
		predicates.add(cb.function("LEFT", String.class, root.get(WareHouse_.geoHash), param1).in(geoHashes));

		cq.where(predicates.toArray(new Predicate[]{}));
		final TypedQuery<WareHouse> query = em.createQuery(cq);
		query.setParameter(param1, geoHashLength);
		return query.getResultList();
	}
}
