package com.centoria.jobmaroc.service.impl;

import com.centoria.jobmaroc.dao.ICityDao;
import com.centoria.jobmaroc.dao.impl.CityDao;
import com.centoria.jobmaroc.model.City;
import com.centoria.jobmaroc.service.ICityService;

import java.util.ArrayList;
import java.util.List;

public class CityService extends BaseService<City, ICityDao> implements ICityService {
    /** Tri MongoDB : nom de ville A→Z (listes déroulantes, filtres, liens). */
    public static final String SORT_BY_NAME_ASC = "{name:1}";

    private static ICityService instance = null;

    private CityService() {
        dao = CityDao.getInstance();
    }

    public static ICityService getInstance() {
        if (instance == null) {
            instance = new CityService();
        }
        return instance;
    }

    @Override
    public City getCityBySlug(String slug) {
        String query = "{slug:'" + slug + "'}";
        List<City> cities = get(query, null, null, -1, -1);
        if (cities != null && !cities.isEmpty()) {
            return cities.get(0);
        }
        return null;
    }

    @Override
    public String getCityNameBySlug(String slug) {
        if (slug == null || slug.isEmpty()) {
            return null;
        }
        // Use MongoDB projection to fetch only the 'name' field (more efficient)
        String query = "{slug:'" + slug + "'}";
        String projection = "{name: 1}";
        List<City> cities = get(query, projection, null, 1, 1);
        if (cities != null && !cities.isEmpty() && cities.get(0).getName() != null) {
            return cities.get(0).getName();
        }
        return null;
    }

    public List<City> getAllCities() {
        List<City> cities = get("{}", null, SORT_BY_NAME_ASC, 0, 40);
        return cities != null ? cities : null;
    }

    @Override
    public City getCityByName(String city) {
        String query = "{" + "name:'" + city + "'}";
        List<City> cities = get(query, null, null, -1, -1);
        if (cities != null && cities.size() > 0) {
            return cities.get(0);
        }
        return null;
    }

    //    @Override
//    public List<City> getCitiesByRegion(String regionSlug) {
//        String query = "{region.slug:'" + regionSlug + "'}";
//        List<City> cities = get(query, null, "{population:-1}", 0, -1);
//        return cities != null ? cities : new ArrayList<>();
//    }
    @Override
    public List<City> getCitiesByRegion(String regionSlug) {
        String query = String.format("{\"region.slug\":\"%s\"}", regionSlug);
        String sort = SORT_BY_NAME_ASC;
        List<City> cities = get(query, null, sort, 0, -1);
        return cities != null ? cities : new ArrayList<>();
    }

}
