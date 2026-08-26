package com.centoria.jobmaroc.dto;

import java.util.Map;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputSearchAdDTO {	
		
	/*
	 * With cities and with Categories is for search form
	 */
	@Builder.Default
	private boolean withCities = false;
	@Builder.Default
	private boolean withCategories = false;
	@Builder.Default
	private boolean withJobs = true;	
	@Builder.Default
	private boolean withDefaultResult = true;
	/*
	 * Links builded for search result page
	 */
	@Builder.Default
	private boolean withCityLinksForDomain = false;
	@Builder.Default
	private String withCityLinksForDomainPrefix = null;
	@Builder.Default
	private String withCityLinksForDomainSuffix = null;
	
	@Builder.Default
	private boolean withDomainLinksForCity = false;
	@Builder.Default
	private String withDomainLinksForCityPrefix = null;
	@Builder.Default
	private String withDomainLinksForCitySuffix = null;	
	
	@Builder.Default
	private boolean withPremiumAds = false;
	@Builder.Default
	private boolean withFirstCities = false;


	@Builder.Default
	private boolean categoryWithJobs = true;
	@Builder.Default
	private boolean withCityLink = false;

		
	private String keyword;
	private String state ;
	private String city;
	private String domain;
	private String id;
	private String secretCode;
	private String email;

	@Builder.Default
	private int page = 1;
	private int numberOfItem;


	private String domainSlug;
	private String citySlug;
	private int numberOfDomain = -1;
	private int numberOfCity = -1;
	private int domainPage = -1;
	private int cityPage = -1;

	
	/*
	 * cette function lire params from request and set value
	 *  in the variable up 
	 */
	public void fillFromParams(Map<String, String> params) {
		if (params.get("page") != null && !params.get("page").isEmpty()){
			page = Integer.parseInt(params.get("page"));
		}
		if (params.get("keyword") != null && !params.get("keyword").isEmpty()){
			keyword = params.get("keyword");
		}
		if (params.get("city") != null && !params.get("city").isEmpty()){
			city = params.get("city");
		}
		if (params.get("domain") != null && !params.get("domain").isEmpty()){
			domain = params.get("domain");
		}	
		if (params.get("state") != null && !params.get("state").isEmpty()){
			state = params.get("state");
		}
		if (params.get("email") != null && !params.get("email").isEmpty()){
			email = params.get("email");
		}		
	}


}
