package com.centoria.jobmaroc.dao.impl;

import com.centoria.jobmaroc.dao.AbstractSimpleGenericDao;
import com.centoria.jobmaroc.dao.ICityDao;
import com.centoria.jobmaroc.model.City;

public class CityDao extends AbstractSimpleGenericDao<City> implements ICityDao {

	private static ICityDao instance = null;
	
	private CityDao() {
		targetClass = City.class;
	}
	
	public static ICityDao getInstance() {
		if (instance == null) {
			instance = new CityDao();
		}
		return instance;
	}
	
}
