package com.centoria.jobmaroc.service;

import com.centoria.jobmaroc.model.City;

import java.util.List;

public interface ICityService extends IBaseService<City> {

    City getCityBySlug(String slug);

    /**
     * Get only the city name by slug using MongoDB projection.
     * More efficient than fetching the entire City object.
     * 
     * @param slug The city slug
     * @return The city name, or null if not found
     */
    String getCityNameBySlug(String slug);

    List<City> getAllCities();

    City getCityByName(String city);

    List<City> getCitiesByRegion(String regionSlug);
}
