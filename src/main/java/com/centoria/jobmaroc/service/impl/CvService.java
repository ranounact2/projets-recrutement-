package com.centoria.jobmaroc.service.impl;

import com.centoria.jobmaroc.dao.ICvDao;
import com.centoria.jobmaroc.dao.impl.CvDao;
import com.centoria.jobmaroc.model.Cv;
import com.centoria.jobmaroc.service.ICvService;

public class CvService extends BaseService<Cv,ICvDao> implements ICvService {

	private static ICvService instance = null;
	
	private CvService() {
		this(CvDao.getInstance());
	}

	/**
	 * Pour les tests : injecte un DAO (ex. mock) sans toucher au singleton production.
	 */
	public CvService(ICvDao cvDao) {
		this.dao = cvDao;
	}
	
	public static ICvService getInstance() {
		if (instance == null) {
			instance = new CvService();
		}
		return instance;
	}

}
