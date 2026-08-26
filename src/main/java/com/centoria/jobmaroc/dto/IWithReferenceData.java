package com.centoria.jobmaroc.dto;

import com.centoria.jobmaroc.model.City;

import java.util.List;

public interface IWithReferenceData {

	List<City> getCities();

	List<DomainDto> getDomains();

	boolean isWithCities();
	
	boolean isWithCategories();
}
