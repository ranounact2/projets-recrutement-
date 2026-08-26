package com.centoria.jobmaroc.app;

import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.common.globalData.UrlConst;
import com.centoria.jobmaroc.dto.*;
import com.centoria.jobmaroc.dto.mapper.MapperAdDto;
import com.centoria.jobmaroc.dto.mapper.MapperDomainDto;
import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.model.City;
import com.centoria.jobmaroc.model.Region;
import com.centoria.jobmaroc.service.*;
import com.centoria.jobmaroc.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class IndexApp {
    private static IndexApp instance = null;
    private IAdService adService = AdService.getInstance();
    private ICityService cityService = CityService.getInstance();
    private IRegionService regionService = RegionService.getInstance();
    private IDomainService domainService = DomainService.getInstance();
    private IZonePageService zonePageService = ZonePageService.getInstance();
    private MapperAdDto mapperJob = Mappers.getMapper(MapperAdDto.class);
    private MapperDomainDto mapperDomain = Mappers.getMapper(MapperDomainDto.class);


    private IndexApp() {
    }

    public static IndexApp getInstance() {
        if (instance == null) {
            instance = new IndexApp();
        }
        return instance;
    }


    public SearchResultAdDto search(InputSearchAdDTO inputDto) {
        IQueryBuilder queryBuilder = new IQueryBuilder() {
            @Override
            public String getQuery(InputSearchAdDTO dto) {
                Map<String, String> map = new HashMap<>();

                String city = inputDto.getCity();
                String domain = inputDto.getDomain();
                String keyword = inputDto.getKeyword();
                String state = inputDto.getState();

                String query = null;

                /*
                 * search by domain/city/keyword
                 *
                 * categorie categories
                 */

                query = "{";
                if (keyword != null) {
                    query += "$text:{$search:\"" + keyword + "\"},";
                }
                if (city != null) {
                    query += "city:'" + city + "',";
                }
                if (domain != null) {
                    query += "domain:'" + domain + "',";
                }
                if (state != null) {
                    query += "state:'" + state + "',";
                }
                query += "enabled: true, state: 'valid' }";

                return query;
            }
        };
        return search(inputDto, queryBuilder);
    }

    public SearchResultAdDto search(InputSearchAdDTO inputDto, IQueryBuilder queryBuilder) {
        SearchResultAdDto sDto = new SearchResultAdDto();
        List<Ad> ads = null;
        int pagenumber = 1;
        int jobsperpage = AbstractResultDto.DEFAULT_PAGE_SIZE;

        if (inputDto == null || inputDto.getPage() == -1) {
            /*
             * No input Dto no pagination retrieve last 8 jobs
             */
            ads = adService.get("{ state: \"" + Ad.VALID + "\",enabled: true}", null,
                    "{announcetype: -1,stateRank: -1, creationDate: -1}", pagenumber, jobsperpage + 1);
        } else {
            pagenumber = inputDto.getPage();
            // num de page pagination
            sDto.setInputdto(inputDto); // set input user for used later in HBS
            String query = queryBuilder.getQuery(inputDto); // check the url and the Query
            //query = "{domain:'Telecommunication'}";
            ads = adService.get(query, null, "{creationDate: -1}", pagenumber, jobsperpage);
        }

        /*
         * With cities and with categories is for search form Here we get default result
         * when nothing is sent
         *
         * TODO : Check result for categories or cities closed to inputs Dont use for MO
         * Put < 0 for the case of pagination
         * and BO
         */
        /* in case of no result is found */
        if (ads == null || ads.isEmpty()) {
            sDto.setNoResult(true);
        }
        if ((ads == null || ads.isEmpty()) && inputDto.isWithDefaultResult()) {
            ads = adService.get("{ state: \"" + Ad.VALID + "\",enabled: true}", null,
                    "{announcetype: -1,stateRank: -1, creationDate: -1}", pagenumber, jobsperpage);
        }
        /* in case you want to remove jobs from the list category page
        add some sort of condition here
        if (inputDto.withJobs() then excute this line
         */
        if (inputDto != null && inputDto.isCategoryWithJobs()) {
            sDto.setJobs(mapperJob.asDtos(ads));
        }
        /* in case we want all cities or wre want to construct the links of the cities */
        if (inputDto != null && (inputDto.isWithCities() || inputDto.isWithCityLinksForDomain())) {
            List<City> cities = cityService.get(null, null, CityService.SORT_BY_NAME_ASC, -1, -1);
            sDto.setCities(cities);
            if (inputDto.isWithCityLinksForDomain()) {
                buildCityLinksForDomain(inputDto, sDto);
            }

        }/* in case we want the categories or the links such as /categorie/:domainSlug/:villeSlug */
        if (inputDto != null && (inputDto.isWithCategories() || inputDto.isWithDomainLinksForCity())) {
            List<DomainDto> domainDtos = mapperDomain.asDtos(domainService.get(null, null, null, -1, -1));
            sDto.setDomains(domainDtos);
            if (inputDto.isWithDomainLinksForCity()) {
                buildDomainLinksForCity(inputDto.getCitySlug(), sDto);
            }
        } /* need revise what this the premiem ads */
        if (inputDto != null && inputDto.isWithPremiumAds()) {
            sDto.setGoldOffers(mapperJob.asDtos(adService.get("{email:'rtsystems.contact@gmail.com', announcetype: 2}",
                    null, "{creationDate: -1}", 1, 3))); // get our offer represented in the type 2
        }


        return sDto;

    }


    private void buildDomainLinksForCity(String citySlug, SearchResultAdDto resultAdDto) {
        if (resultAdDto != null && resultAdDto.getDomains() != null) {
            List<DomainDto> domainDtos = resultAdDto.getDomains();
            List<LinkDTO> domainLinks = domainDtos.stream().map(domain -> {
                String link = "/" + UrlConst.CATEGORY;
                link += "/" + domain.getSlug() + "/";
                link += citySlug + "/";
                LinkDTO linkDTO = LinkDTO.builder().link(link).label(domain.getName()).icon(domain.getIcon()).build();
                return linkDTO;
            }).collect(Collectors.toList());
            resultAdDto.setDomainLinks(domainLinks);
        }
    }

    private void buildCityLinksForDomain(InputSearchAdDTO inputDto, SearchResultAdDto resultAdDto) {
        if (resultAdDto != null && resultAdDto.getCities() != null) {
            List<City> Citys = resultAdDto.getCities();
            List<LinkDTO> cityLinks = Citys.stream().map(city -> {
                String link = "/" + UrlConst.CATEGORY + "/";
                link += inputDto.getDomainSlug();
                link += "/" + city.getSlug() + "/";
                LinkDTO linkDTO = LinkDTO.builder().link(link).label(city.getName()).build();
                return linkDTO;
            }).collect(Collectors.toList());
            resultAdDto.setCityLinks(cityLinks);
        }
    }

    public SearchResultAdDto indexPage(int pageNumber, int numberoFJobs) {
        SearchResultAdDto searchResultAdDto = new SearchResultAdDto();
        List<DomainDto> domainDtos = domainService.getAllDomain();
        List<City> Citys = cityService.getAllCities();
        List<Region> regions = regionService.getAllRegions();
        List<AdDto> adDtos = adService.getAdsByDefaults(pageNumber, numberoFJobs);
        searchResultAdDto.setJobs(adDtos);
        searchResultAdDto.setDomains(domainDtos);
        searchResultAdDto.setCities(Citys);
        searchResultAdDto.setRegions(regions);
        return searchResultAdDto;
    }


    public SearchResultAdDto cityAdPage(String citySlug, int pageNumber, int numberOfAds) {
        City CityTarget = null;
        SearchResultAdDto searchResultAdDto = new SearchResultAdDto();
        List<DomainDto> domainDtos = domainService.getAllDomain();
        List<City> Citys = cityService.getAllCities();
        CityTarget = cityService.getCityBySlug(citySlug);
        if (CityTarget == null) throw new BusinessException("page not found");
        searchResultAdDto.setCities(Citys);
        searchResultAdDto.setDomains(domainDtos);
        buildDomainLinksForCity(citySlug, searchResultAdDto);
        return searchResultAdDto;
    }


    public SearchResultAdDto searchAdsByKeyword(String keyword, int pageNumber, int numberOfAds) {
        SearchResultAdDto searchResultAdDto = new SearchResultAdDto();
        List<DomainDto> domainDtos = domainService.getAllDomain();
        List<City> Citys = cityService.getAllCities();
        List<AdDto> adDtos = adService.getAdsByKeyword(keyword, pageNumber, numberOfAds);
        if (adDtos == null || adDtos.isEmpty()) searchResultAdDto.setNoResult(false);
        searchResultAdDto.setDomains(domainDtos);
        searchResultAdDto.setCities(Citys);
        searchResultAdDto.setJobs(adDtos);
        return searchResultAdDto;
    }


}
