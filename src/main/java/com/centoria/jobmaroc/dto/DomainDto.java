package com.centoria.jobmaroc.dto;

import com.centoria.jobmaroc.model.Domain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class DomainDto extends Domain{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String link;

}