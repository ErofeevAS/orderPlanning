package group.itechart.orderplanning.service.converter;

import java.util.List;


public interface Converter<D, E> {

	D toDto(E entity);

	E toEntity(D dto);

	List<D> toDtos(List<E> entities);

	List<E> toEntities(List<D> dtos);

}
