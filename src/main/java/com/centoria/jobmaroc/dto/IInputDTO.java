package com.centoria.jobmaroc.dto;

import java.util.Map;

public interface IInputDTO {

	/**
	 * Return fields in form
	 * 
	 * Need for example an String fields[] attribute
	 * @return
	 */
	String[] getFields();

	String[] getSessionFields();

	String[] getUrlFields();

	String[] getHeaderFields();

	/**
	 * Return attributes names in dto for fields
	 * fields[i] go in attributes[i]
	 * 
	 * Need for example an String attributes[] attribute
	 * @return
	 */
	String[] getAttributes();

	String[] getSessionAttributes();

	String[] getUrlAttributes();

	String[] getHeaderAttributes();

	Map<String, String> validate();

	void fill(Map<String, String[]> params, Map<String, String> urlParams, Map<String, String> headerParams,
			Map<String, Object> sessionAttributes);

}