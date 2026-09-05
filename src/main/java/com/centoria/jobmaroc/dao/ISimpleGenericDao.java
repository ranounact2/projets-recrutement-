package com.centoria.jobmaroc.dao;

import java.util.List;

import com.centoria.jobmaroc.model.AbstractModel;

public interface ISimpleGenericDao<T extends AbstractModel> {

	public T find(String id);

	public List<T> find(String query, String projection,  String sort, int pageSize, int start);

	public List<T> search(String query);
	
	public List<T> aggregate(List<String> pipelineStagesJson);
	
	public void delete(T obj);

	public void delete(String id);

	public void delete(List<T> objs);

	public List<String> addOrUpdate(List<T> objs);

	public String addOrUpdate(T t);
	
	public long count(String query);

}



