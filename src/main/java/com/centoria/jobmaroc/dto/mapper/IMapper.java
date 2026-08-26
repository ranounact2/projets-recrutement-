package com.centoria.jobmaroc.dto.mapper;

import java.util.List;

public interface IMapper<E,D> {

	D asDto(E entity);
	
	List<D> asDtos(List<E> entities);

	E asEntity(D dto);

	List<E> asEntities(List<D> dto);

}
