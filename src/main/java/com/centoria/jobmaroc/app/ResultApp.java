package com.centoria.jobmaroc.app;

import com.centoria.jobmaroc.common.globalData.UrlConst;
import com.centoria.jobmaroc.dto.AdDto;
import com.centoria.jobmaroc.dto.DomainDto;
import com.centoria.jobmaroc.dto.LinkDTO;
import com.centoria.jobmaroc.dto.SearchResultAdDto;
import com.centoria.jobmaroc.model.City;
import com.centoria.jobmaroc.model.exception.ElementNotFoundException;
import com.centoria.jobmaroc.service.IAdService;
import com.centoria.jobmaroc.service.ICityService;
import com.centoria.jobmaroc.service.IDomainService;
import com.centoria.jobmaroc.service.impl.AdService;
import com.centoria.jobmaroc.service.impl.CityService;
import com.centoria.jobmaroc.service.impl.DomainService;

import java.util.List;

public class ResultApp {


    private static ResultApp instance = null;
    private IDomainService domainService = DomainService.getInstance();
    private ICityService cityService = CityService.getInstance();
    private IAdService adService = AdService.getInstance();
    private final BaseApp baseApp = BaseApp.getInstance();

    public static ResultApp getInstance() {
        if (instance == null) {
            instance = new ResultApp();
        }
        return instance;
    }

    public SearchResultAdDto getResultByCityCategory(String domainSlug,
                                                     String citySlug,
                                                     String regionSlug,
                                                     int page,
                                                     int itemPage,
                                                     String keyword)
            {

        SearchResultAdDto result = domainPageCity(domainSlug, citySlug, page, itemPage, keyword);

        List<LinkDTO> breadCrumb = baseApp.buildBreadCrumb(domainSlug, citySlug, regionSlug, "/" + UrlConst.CATEGORY);
        result.setBreadCrumb(breadCrumb);

        return result;
    }

    private SearchResultAdDto domainPageCity(String domainSlug,
                                             String citySlug,
                                             int pageNumber,
                                             int itemPage,
                                             String keyword)
            {

        SearchResultAdDto searchResultAdDto = new SearchResultAdDto();
        DomainDto domainTarget = domainService.getDomainBySlug(domainSlug);
        City cityTarget = cityService.getCityBySlug(citySlug);

        if (domainTarget == null || cityTarget == null) {
            throw new ElementNotFoundException(404, "page not found");
        }

        // Utiliser les slugs car c'est ce qui est stocké dans la base de données
        List<AdDto> adDtos = ((AdService) adService).getAdsWithDomainAndCityByName(
            cityTarget.getSlug(),
            domainTarget.getSlug(),
            pageNumber,
            itemPage,
            keyword);
        long totalResults = ((AdService) adService).countAdsWithDomainAndCityByName(
            cityTarget.getSlug(),
            domainTarget.getSlug(),
            keyword);
        int effectivePageSize = itemPage > 0 ? itemPage : SearchResultAdDto.DEFAULT_PAGE_SIZE;
        int totalPages = totalResults == 0 ? 0 : (int) Math.ceil((double) totalResults / effectivePageSize);

        if (adDtos == null || adDtos.isEmpty()) {
            searchResultAdDto.setNoResult(true);
            adDtos = null;  // Don't show default ads, keep it null/empty to trigger no results page
        }

        searchResultAdDto.setPageNumber(pageNumber);
        searchResultAdDto.setPageSize(effectivePageSize);
        searchResultAdDto.setTotalCount((int) Math.min(Integer.MAX_VALUE, totalResults));
        searchResultAdDto.setTotalPages(totalPages);
        searchResultAdDto.setCities(cityService.getAllCities());
        searchResultAdDto.setDomains(domainService.getAllDomain());
        searchResultAdDto.setJobs(adDtos);

        return searchResultAdDto;
    }

    public SearchResultAdDto getAdsAndCitiesByRegion(String domainSlug, String regionSlug, int page, int itemPage) {

        SearchResultAdDto searchResultAdDto = new SearchResultAdDto();
        List<City> cities = cityService.getCitiesByRegion(regionSlug);
        List<AdDto> ads = AdService.getInstance().getAdsByRegion(domainSlug, regionSlug, page, itemPage);

        searchResultAdDto.setPageNumber(page);
        searchResultAdDto.setPageSize(itemPage);
        searchResultAdDto.setCities(cities);
        searchResultAdDto.setJobs(ads);
        return searchResultAdDto;
    }
}
