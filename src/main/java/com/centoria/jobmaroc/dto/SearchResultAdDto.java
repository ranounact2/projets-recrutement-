package com.centoria.jobmaroc.dto;

import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.model.City;
import com.centoria.jobmaroc.model.PageZones;
import com.centoria.jobmaroc.model.Region;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SearchResultAdDto extends AbstractResultDto {

    private List<DomainDto> domains = null;

    private List<City> cities = null;

    private List<Region> regions = null;

    private List<City> firstCities = null;

    private List<AdDto> jobs = null;

    private List<AdDto> goldOffers = null;

    private InputSearchAdDTO inputdto = null;

    private List<LinkDTO> cityLinks = null;

    private List<LinkDTO> domainLinks = null;

    private List<Ad> ads = null;

    private List<PageZones> zones;
    private AdDto job;
    private List<LinkDTO> breadCrumb;
    private int pageNumber;
    private int pageSize;
    /*
     *
     * When no result is found minimal jobs
     * are shown and this field is set to true
     */
    private boolean noResult = false;

}
