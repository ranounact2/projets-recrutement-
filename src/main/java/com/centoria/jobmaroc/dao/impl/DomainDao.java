package com.centoria.jobmaroc.dao.impl;

import com.centoria.jobmaroc.dao.AbstractSimpleGenericDao;
import com.centoria.jobmaroc.dao.IDomainDao;
import com.centoria.jobmaroc.model.Domain;


public class DomainDao extends AbstractSimpleGenericDao<Domain> implements IDomainDao {

	private static IDomainDao instance = null;
	
	private DomainDao() {
		targetClass = Domain.class;
	}
	public static IDomainDao getInstance() {
		if (instance == null) {
			instance = new DomainDao();
		}
		return instance;
	}

}
