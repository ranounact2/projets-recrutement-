package com.centoria.jobmaroc.dto;

import java.util.List;
import java.util.Map;

import com.centoria.jobmaroc.model.Ad;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdDisplayDto extends AbstractInputDTO {

	private Boolean withOffers;

	private String inputKey;
	
	private AdDto job;
	
	private List<Ad> similarJobs = null;
	private List<AdDto> similarOffre = null;

	private Map<String, String> errors;
	
	@Override
	public String[] getFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getSessionFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getUrlFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getHeaderFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getSessionAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getUrlAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getHeaderAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> validate() {
		return null;
	}

	
}
