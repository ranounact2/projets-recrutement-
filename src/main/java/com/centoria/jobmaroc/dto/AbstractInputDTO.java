package com.centoria.jobmaroc.dto;

import java.lang.reflect.Field;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractInputDTO implements IInputDTO {
	
	/**
	 * Return fields in form
	 * 
	 * Need for example an String fields[] attribute
	 * @return
	 */
	@Override
	public abstract String[] getFields();

	@Override
	public abstract String[] getSessionFields();

	@Override
	public abstract String[] getUrlFields();

	@Override
	public abstract String[] getHeaderFields();

	/**
	 * Return attributes names in dto for fields
	 * fields[i] go in attributes[i]
	 * 
	 * Need for example an String attributes[] attribute
	 * @return
	 */
	@Override
	public abstract String[] getAttributes();
	
	@Override
	public abstract String[] getSessionAttributes();

	@Override
	public abstract String[] getUrlAttributes();

	@Override
	public abstract String[] getHeaderAttributes();

	@Override
	public abstract Map<String, String> validate();

	@Override
	public void fill(Map<String, String[]> params, Map<String, String> urlParams, Map<String, String> headerParams, Map<String, Object> sessionAttributes) {
		if (params != null && getFields() != null) {
			for (int i = 0; i < getFields().length; i++) {
				try {
					Field f = this.getClass().getField(getAttributes()[i]);
					if (params.get(getFields()[i]) != null &&  params.get(getFields()[i]).length == 1) {
						f.set(this, params.get(getFields()[i])[0]);
					}
				} catch (NoSuchFieldException e) {
					log.error("Field not found: {}", getAttributes()[i], e);
				} catch (SecurityException e) {
					log.error("Security error accessing field: {}", getAttributes()[i], e);
				} catch (IllegalArgumentException e) {
					log.error("Illegal argument for field: {}", getAttributes()[i], e);
				} catch (IllegalAccessException e) {
					log.error("Illegal access to field: {}", getAttributes()[i], e);
				}
			}
		}
	}
}
