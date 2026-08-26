package com.centoria.jobmaroc.dto.mapper;

import com.centoria.jobmaroc.dto.RegionDTO;
import com.centoria.jobmaroc.model.Region;
import org.mapstruct.Mapper;

@Mapper
public interface MapperRegionDto extends IMapper<Region, RegionDTO> {
}
