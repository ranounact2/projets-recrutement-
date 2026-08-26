package com.centoria.jobmaroc.service.impl;

import java.util.List;

import com.centoria.jobmaroc.dao.ISimpleGenericDao;
import com.centoria.jobmaroc.model.AbstractModel;
import com.centoria.jobmaroc.service.IBaseService;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public abstract class BaseService<T extends AbstractModel, U extends ISimpleGenericDao<T>> implements IBaseService<T> {

	protected U dao = null;
	
	@Override
	public void addOrUpdate(T obj) {
		dao.addOrUpdate(obj);
	}

	@Override
	public void addOrUpdate(List<T> ts) {
		dao.addOrUpdate(ts);
	}

	@Override
	public T get(String id) {
		return dao.find(id);
	}
	
	@Override
	public long count(String query) {
		return dao.count(query);
	}
	
	@Override
	public List<T> get(String query, String projection, String sort, int pagenumber, int pagesize) {
		List<T> result = null;
		result = dao.find(query, projection, sort, pagenumber, pagesize);
		return result;
	}

	@Override
	public List<T> search(String query) {
		List<T> result = null;
		result = dao.search(query);
		return result;
	}

	@Override
	public void delete(String id) {
		dao.delete(id);
	}

	@Override
	public void delete(T obj) {
		dao.delete(obj);
	}

}
