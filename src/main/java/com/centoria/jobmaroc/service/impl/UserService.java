package com.centoria.jobmaroc.service.impl;

import com.centoria.jobmaroc.dao.IUserDao;
import com.centoria.jobmaroc.dao.impl.UserDao;
import com.centoria.jobmaroc.model.security.User;
import com.centoria.jobmaroc.service.IUserService;

public class UserService extends BaseService<User, UserDao> implements IUserService {
	
	private IUserDao userDao = UserDao.getInstance();
	
	private static IUserService instance = new UserService();
	
	private UserService() {
	}

	public static IUserService getIntance() {
		if (instance == null) {	
			instance = new UserService();
		}
		return instance;
	}



}
