package com.centoria.jobmaroc.dto.mapper;

import org.mapstruct.Mapper;

import com.centoria.jobmaroc.dto.AdDto;
import com.centoria.jobmaroc.model.Ad;

@Mapper
public interface MapperAdDto extends IMapper<Ad, AdDto> {

	
}
