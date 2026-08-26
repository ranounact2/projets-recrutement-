package com.centoria.jobmaroc.dto.mapper;

import org.mapstruct.Mapper;

import com.centoria.jobmaroc.dto.DomainDto;
import com.centoria.jobmaroc.model.Domain;

@Mapper
public interface MapperDomainDto extends IMapper<Domain, DomainDto> {

	
}
