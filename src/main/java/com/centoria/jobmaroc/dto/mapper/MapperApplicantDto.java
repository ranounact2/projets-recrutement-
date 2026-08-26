package com.centoria.jobmaroc.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.centoria.jobmaroc.dto.ApplicantDto;
import com.centoria.jobmaroc.model.Applicant;

@Mapper
public interface MapperApplicantDto extends IMapper<Applicant, ApplicantDto> {

	@Override
	@Mapping(target = "cv", ignore = true)
	Applicant asEntity(ApplicantDto dto);
}
