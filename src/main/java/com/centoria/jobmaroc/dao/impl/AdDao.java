package com.centoria.jobmaroc.dao.impl;


import com.centoria.jobmaroc.dao.AbstractSimpleGenericDao;
import com.centoria.jobmaroc.dao.IAdDao;
import com.centoria.jobmaroc.model.Ad;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdDao extends AbstractSimpleGenericDao<Ad> implements IAdDao {

	private static IAdDao instance = null;

	private AdDao() {
		targetClass = Ad.class;		
	}
	
	public static IAdDao getInstance() {
		if (instance == null) {
			instance = new AdDao();
		}
		return instance;
	}

}