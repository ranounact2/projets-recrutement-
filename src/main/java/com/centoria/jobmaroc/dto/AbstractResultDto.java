package com.centoria.jobmaroc.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractResultDto {
	
	/*
	 * Erreurs et autres informations
	 */
	protected List<String> errors = null;

	protected List<String> warnings = null;

	protected List<String> infos = null;

	protected Map<String, String> fieldErrors = null;

	protected Map<String,String> messages = new HashMap<>();

	/*
	 * Informations de pagination
	 */
	private int pageSize = -1;
	
	private int start = -1;
	
	private int totalCount = -1;

	private int pageNumber = -1;
	
	private int totalPages = -1;	

	private LinkDTO prev;
	
	private LinkDTO next;
	
	public static final int DEFAULT_PAGE_SIZE = 8;

}

