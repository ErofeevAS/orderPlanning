package group.itechart.orderplanning.service.converter.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import group.itechart.orderplanning.service.converter.Converter;


@Component
abstract public class AbstractConverter<D,E> implements Converter<D,E> {

	private Class<D> dtoClass = (Class<D>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	private Class<E> entityClass = (Class<E>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];

	protected final ModelMapper modelMapper;

	protected AbstractConverter(final ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	@Override
	public D toDto(final E entity) {
		return modelMapper.map(entity, dtoClass);
	}

	@Override
	public E toEntity(final D dto) {
		return modelMapper.map(dto, entityClass);
	}

	@Override
	public List<D> toDtos(final List<E> entities) {
		return entities.stream().map(this::toDto).collect(Collectors.toList());
	}


}
