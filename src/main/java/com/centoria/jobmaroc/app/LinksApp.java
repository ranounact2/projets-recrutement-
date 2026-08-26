package com.centoria.jobmaroc.app;

import com.centoria.jobmaroc.common.globalData.UrlConst;
import com.centoria.jobmaroc.dto.AdDto;
import com.centoria.jobmaroc.dto.DomainDto;
import com.centoria.jobmaroc.dto.LinkDTO;
import com.centoria.jobmaroc.dto.SearchResultAdDto;
import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.model.City;
import com.centoria.jobmaroc.model.Region;
import com.centoria.jobmaroc.model.exception.ElementNotFoundException;
import com.centoria.jobmaroc.service.IAdService;
import com.centoria.jobmaroc.service.ICityService;
import com.centoria.jobmaroc.service.IDomainService;
import com.centoria.jobmaroc.service.IRegionService;
import com.centoria.jobmaroc.service.impl.AdService;
import com.centoria.jobmaroc.service.impl.CityService;
import com.centoria.jobmaroc.service.impl.DomainService;
import com.centoria.jobmaroc.service.impl.RegionService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LinksApp {
    private static LinksApp instance = null;
    private IRegionService regionService = RegionService.getInstance();
    private IDomainService domainService = DomainService.getInstance();
    private ICityService cityService = CityService.getInstance();
    private IAdService adService = AdService.getInstance();

    public static LinksApp getInstance() {
        if (instance == null) {
            instance = new LinksApp();
        }
        return instance;
    }

    public List<Region> getAllRegion() {
        return regionService.getAllRegions();
    }

    public List<Ad> getAllAds() {
        return adService.getAllAds();
    }

    public SearchResultAdDto allDomainsPage() {
        SearchResultAdDto searchResultAdDto = new SearchResultAdDto();
        List<DomainDto> domainDtos = domainService.getAllDomain();
        searchResultAdDto.setDomains(domainDtos);
        buildDomainLinks(searchResultAdDto);
        return searchResultAdDto;
    }

    public SearchResultAdDto allDomainsAndAdsPage() {
        SearchResultAdDto searchResultAdDto = new SearchResultAdDto();
        List<DomainDto> domainDtos = domainService.getAllDomain();
        List<Ad> ads = adService.getAllAds();
        searchResultAdDto.setDomains(domainDtos);
        searchResultAdDto.setAds(ads);
        buildDomainLinks(searchResultAdDto);

        return searchResultAdDto;
    }

    public void buildDomainLinks(SearchResultAdDto resultAdDto) {
        if (resultAdDto != null && resultAdDto.getDomains() != null) {
            List<DomainDto> domainDtos = resultAdDto.getDomains();
            List<LinkDTO> domainLinks = domainDtos.stream().map(domain -> {
                String link = "/" + UrlConst.CATEGORY;
                link += "/" + domain.getSlug();
                LinkDTO linkDTO = LinkDTO.builder().link(link).label(domain.getName()).icon(domain.getIcon()).build();
                return linkDTO;
            }).collect(Collectors.toList());
            resultAdDto.setDomainLinks(domainLinks);
        }
    }

    /* build links for the cities link: /city/:citySlug */
    public void buildCityLinks(SearchResultAdDto resultAdDto) {
        if (resultAdDto != null && resultAdDto.getCities() != null) {
            List<LinkDTO> citiesLinks = resultAdDto.getCities().stream().map(city -> {
                String link = "/" + UrlConst.VILLE;
                link += "/" + city.getSlug() + "/";
                LinkDTO linkDTO = LinkDTO.builder().link(link).label(city.getName()).build();
                return linkDTO;
            }).collect(Collectors.toList());
            resultAdDto.setCityLinks(citiesLinks);
        }
    }

    public SearchResultAdDto allCitiesPage() {
        SearchResultAdDto searchResultAdDto = new SearchResultAdDto();
        List<DomainDto> domainDtos = domainService.getAllDomain();
        List<City> Citys = cityService.getAllCities();
        searchResultAdDto.setCities(Citys);
        searchResultAdDto.setDomains(domainDtos);
        buildCityLinks(searchResultAdDto);
        buildDomainLinks(searchResultAdDto);
        return searchResultAdDto;
    }

    private void buildDomainLinksFromCities(String domainSlug, List<City> CityList, SearchResultAdDto resultAdDto) {
        if (CityList != null) {
            List<LinkDTO> cityLinks = CityList.stream().map(city -> {
                String link = "/" + UrlConst.CATEGORY + "/";
                link += domainSlug;
                link += "/" + city.getSlug() + "/";
                LinkDTO linkDTO = LinkDTO.builder().link(link).label(city.getName()).build();
                return linkDTO;
            }).collect(Collectors.toList());
            resultAdDto.setCityLinks(cityLinks);
        }
    }


    public SearchResultAdDto singleDomainPage(String domainSlug, int page, int itemPage, String keyword) {
        SearchResultAdDto searchResultAdDto = new SearchResultAdDto();
        List<City> allCities = cityService.getAllCities();
        List<Region> allRegion = regionService.getAllRegions();

        searchResultAdDto.setCities(allCities);
        searchResultAdDto.setRegions(allRegion);
        DomainDto domainTarget = domainService.getDomainBySlug(domainSlug);

        if (domainTarget == null) {
            throw new ElementNotFoundException(404, "page not found");
        }
        
        // Ajouter tous les domaines pour le filtre dans le template
        List<DomainDto> domains = domainService.getAllDomain();
        searchResultAdDto.setDomains(domains);
        buildDomainLinks(searchResultAdDto);

        // Utiliser getAddsByDomain avec le slug du domaine (car c'est ce qui est stocké en DB)
        List<AdDto> ads = adService.getAddsByDomain(domainTarget.getSlug(), page, itemPage, keyword);
        searchResultAdDto.setJobs(ads);
        searchResultAdDto.setPageNumber(page);
        searchResultAdDto.setPageSize(itemPage);
        
        if (ads == null || ads.isEmpty()) {
            searchResultAdDto.setNoResult(true);
        }
        
        searchResultAdDto.setCities(allCities);
        buildDomainLinksFromCities(domainSlug, allCities, searchResultAdDto);
        return searchResultAdDto;
    }

    public List<City> getCitiesByRegion(String regionSlug) {
        return cityService.getCitiesByRegion(regionSlug);
    }

    public SearchResultAdDto getCitiesAndAdsByRegion(String regionSlug, int page, int itemPage)
            {
        return getCitiesAndAdsByRegion(regionSlug, page, itemPage, null);
    }

    public SearchResultAdDto getCitiesAndAdsByRegion(String regionSlug, int page, int itemPage, String domainSlug)
            {

        List<City> cities = cityService.getCitiesByRegion(regionSlug);
        List<Ad> ads = (domainSlug == null || domainSlug.isEmpty())
            ? adService.getAdsByRegions(regionSlug, page, itemPage)
            : adService.getAdsByRegionsAndDomain(regionSlug, page, itemPage, domainSlug);
        List<DomainDto> domains = domainService.getAllDomain();

        SearchResultAdDto result = new SearchResultAdDto();
        result.setCities(cities);
        result.setAds(ads);
        result.setDomains(domains);
        result.setPageNumber(page);
        result.setPageSize(itemPage);

        if (ads == null || ads.isEmpty()) {
            result.setNoResult(true);
        }

        buildCityLinks(result);

        return result;
    }

    public SearchResultAdDto getDomainsAndAdsByRegionCity(String regionSlug,
                                                          String citySlug,
                                                          int page,
                                                          int itemPage,
                                                          String keyword)
            {

        SearchResultAdDto result = new SearchResultAdDto();
        result.setPageNumber(page);
        result.setPageSize(itemPage);

        List<DomainDto> domains = domainService.getAllDomain();
        result.setDomains(domains);
        buildDomainLinks(result);

        if (citySlug == null || citySlug.isEmpty()) {
            result.setAds(Collections.emptyList());
            result.setNoResult(true);
            return result;
        }

        City city = cityService.getCityBySlug(citySlug);
        if (city == null || city.getRegion() == null) {
            result.setAds(Collections.emptyList());
            result.setNoResult(true);
            return result;
        }

        if (regionSlug != null && !regionSlug.isEmpty()) {
            Region cityRegion = city.getRegion();
            if (cityRegion == null || !regionSlug.equals(cityRegion.getSlug())) {
                result.setAds(Collections.emptyList());
                result.setNoResult(true);
                return result;
            }
        }

        List<Ad> ads = adService.getAdsByCity(city.getName(), page, itemPage, keyword);
        if (ads == null || ads.isEmpty()) {
            result.setAds(Collections.emptyList());
            result.setNoResult(true);
        } else {
            result.setAds(ads);
        }

        return result;
    }
}
