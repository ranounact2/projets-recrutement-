package com.centoria.jobmaroc.dao.impl;

import com.centoria.jobmaroc.dao.AbstractSimpleGenericDao;
import com.centoria.jobmaroc.dao.ICvDao;
import com.centoria.jobmaroc.model.Cv;

public class CvDao extends AbstractSimpleGenericDao<Cv> implements ICvDao {

	private static ICvDao instance = null;
	
	private CvDao() {
		targetClass = Cv.class;
	}
	
	public static ICvDao getInstance() {
		if (instance == null) {
			instance = new CvDao();
		}
		return instance;
	}
	
}
