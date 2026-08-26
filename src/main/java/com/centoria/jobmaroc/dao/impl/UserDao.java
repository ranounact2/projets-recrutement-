package com.centoria.jobmaroc.dao.impl;

import com.centoria.jobmaroc.dao.AbstractSimpleGenericDao;
import com.centoria.jobmaroc.dao.IUserDao;
import com.centoria.jobmaroc.model.security.User;



public class UserDao extends AbstractSimpleGenericDao<User> implements IUserDao {

	private static IUserDao instance = null;

	private UserDao() {
		targetClass = User.class;
	}

	public static IUserDao getInstance() {
		if (instance == null) {
			instance = new UserDao();

		}
		return instance;
	}


}
