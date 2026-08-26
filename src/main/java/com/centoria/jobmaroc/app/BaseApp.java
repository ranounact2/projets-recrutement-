package com.centoria.jobmaroc.app;

import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.common.globalData.UrlConst;
import com.centoria.jobmaroc.dto.AdDto;
import com.centoria.jobmaroc.dto.DomainDto;
import com.centoria.jobmaroc.dto.LinkDTO;
import com.centoria.jobmaroc.dto.MailDto;
import com.centoria.jobmaroc.dto.mapper.MapperAdDto;
import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.model.City;
import com.centoria.jobmaroc.model.Region;
import com.centoria.jobmaroc.service.*;
import com.centoria.jobmaroc.service.impl.*;
import com.centoria.jobmaroc.service.MailServiceFactory;
import org.mapstruct.factory.Mappers;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class BaseApp {
    private static BaseApp instance = null;
    private ICityService cityService = CityService.getInstance();
    private IDomainService domainService = DomainService.getInstance();
    private IRegionService regionService = RegionService.getInstance();
    private IAdService adService = AdService.getInstance();
    private IMailService mailService = MailServiceFactory.getInstance();
    private MapperAdDto mapperJob = Mappers.getMapper(MapperAdDto.class);

    public static BaseApp getInstance() {
        if (instance == null) {
            instance = new BaseApp();
        }
        return instance;
    }

    public List<LinkDTO> buildBreadCrumb(String domainSlug, String citySlug, String regionSlug, String currentRoute) {
        List<LinkDTO> breadCrumbDto = new LinkedList<>();
        breadCrumbDto.add(new LinkDTO("/", null, "Accueil"));

        if (currentRoute.startsWith("/region")) {
            breadCrumbDto.add(new LinkDTO("/region", null, UrlConst.REGION));

            if (regionSlug != null) {
                Region region = regionService.getRegionBySlug(regionSlug);
                breadCrumbDto.add(new LinkDTO("/region/" + regionSlug, null, region.getName()));
            }

            if (citySlug != null) {
                City city = cityService.getCityBySlug(citySlug);
                breadCrumbDto.add(new LinkDTO("/region/" + regionSlug + "/" + citySlug, null, city.getName()));
            }

            if (domainSlug != null) {
                DomainDto targetDomain = domainService.getDomainBySlug(domainSlug);
                breadCrumbDto.add(new LinkDTO("/region/" + regionSlug + "/" + citySlug + "/" + domainSlug, null, targetDomain.getName()));
            }

        } else if (currentRoute.startsWith("/categorie")) {
            breadCrumbDto.add(new LinkDTO("/categorie", null, UrlConst.CATEGORY));

            if (domainSlug != null) {
                DomainDto targetDomain = domainService.getDomainBySlug(domainSlug);
                breadCrumbDto.add(new LinkDTO("/categorie/" + domainSlug, null, targetDomain.getName()));
            }

            if (regionSlug != null) {
                Region region = regionService.getRegionBySlug(regionSlug);
                breadCrumbDto.add(new LinkDTO("/categorie/" + domainSlug + "/" + regionSlug, null, region.getName()));
            }

            if (citySlug != null) {
                City city = cityService.getCityBySlug(citySlug);
                breadCrumbDto.add(new LinkDTO("/categorie/" + domainSlug + "/" + regionSlug + "/" + citySlug, null, city.getName()));
            }
        }

        return breadCrumbDto;
    }


    public List<Integer> getPagination(String domainSlug, String citySlug, int itemPage) {
        List<Integer> listPagination = new ArrayList<>();
        DomainDto domainTarget = domainService.getDomainBySlug(domainSlug);
        City CityTarget = cityService.getCityBySlug(citySlug);
        // Use slug for city field in database query (city field now stores slug)
        String query = "{\"domain\":\"" + domainTarget.getName() + "\",\"city\":\"" + CityTarget.getSlug() + "\",\"state\":\"" + Ad.VALID + "\"}";
        Long allAdsPageNumber = (long) Math.ceil((double) adService.count(query) / itemPage);
        for (long i = 1; i <= allAdsPageNumber; i++) {
            listPagination.add((int) i);
        }
        return listPagination;
    }
    /*
     * send email when state is changed from new_verified to valid emailTemplate
     * params it's used to determine the template
     */
    protected void sendEmail(Ad ad, String host, String emailTemplate) {
        AdDto jobdto = mapperJob.asDto(ad);
        MailDto<AdDto> mdto = new MailDto<AdDto>(host, jobdto, null);
        adService.renderTemplate(mdto, emailTemplate);
        mailService.sendMail(jobdto.getEmail(), null, mdto.getSubject(), mdto.getMailContent(), null, null);
    }

    /**
     * Validates an advertisement by updating its state and sending a validation email.
     *
     * @param _id  The ID of the advertisement to validate.
     * @param host The host for constructing the email link.
     */
    public void validate(String _id, String host) {
        Ad ad = adService.get(_id);
        ad.setState(Ad.VALID);
        ad.setStateRank(Ad.VALID_RANK);
        adService.addOrUpdate(ad);
        String emailTemplate = "validate";

        CompletableFuture<Void> mailFuture = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    sendEmail(ad, host, emailTemplate);
                } catch (TechnicalException | BusinessException e) {
                    log.error("Error sending validation email for ad: {}", _id, e);
                }
            }
        }).thenRun(() -> {
        });

    }

}
