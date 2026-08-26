package com.centoria.jobmaroc.service;

import java.util.List;

import com.centoria.jobmaroc.model.AbstractModel;

public interface IBaseService<T extends AbstractModel> {

	void addOrUpdate(T obj);
	
	void addOrUpdate(List<T> ts);

	T get(String id);

	List<T> get(String query, String projection, String sort, int pageSize, int start);
	
	List<T> search(String query);

	long count(String query);
	
	void delete(String id);

	void delete(T obj);

}
