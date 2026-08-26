package com.centoria.jobmaroc.dao.impl;

import com.centoria.jobmaroc.dao.AbstractSimpleGenericDao;
import com.centoria.jobmaroc.dao.IZonePageDao;
import com.centoria.jobmaroc.model.PageZones;

public class ZonePageDao extends AbstractSimpleGenericDao<PageZones> implements IZonePageDao {

	private static IZonePageDao instance = null;
	
	private ZonePageDao() {
		targetClass = PageZones.class;
	}
	
	public static IZonePageDao getInstance() {
		if (instance == null) {
			instance = new ZonePageDao();
		}
		return instance;
	}
	
}
