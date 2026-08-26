package com.centoria.jobmaroc.app;

import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.dto.AdDto;
import com.centoria.jobmaroc.dto.AdResult;
import com.centoria.jobmaroc.dto.InputSearchAdDTO;
import com.centoria.jobmaroc.dto.MailDto;
import com.centoria.jobmaroc.dto.mapper.MapperAdDto;
import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.service.IAdService;
import com.centoria.jobmaroc.service.IMailService;
import com.centoria.jobmaroc.service.impl.AdService;
import com.centoria.jobmaroc.service.MailServiceFactory;
import org.mapstruct.factory.Mappers;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Slf4j
public  class BoApp extends BaseApp {

    /** Valeur du paramètre URL {@code filter} : toutes les annonces. */
    public static final String BO_STATUS_FILTER_ALL = "all";
    /** Annonces nouvelles : {@code state} {@code new} ou {@code new_verified}. */
    public static final String BO_STATUS_FILTER_NEW = "new";
    /** Annonces modifiées : {@code state} {@code updated} ou {@code updated_verified}. */
    public static final String BO_STATUS_FILTER_MODIFIED = "modified";

    private static BoApp instance = null;
    private IAdService adService = AdService.getInstance();
    private IQueryBuilder queryBuilder = new BoAppQueryBuilder();

    private IMailService mailService = MailServiceFactory.getInstance();

    private MapperAdDto mapperJob = Mappers.getMapper(MapperAdDto.class);

    private class BoAppQueryBuilder implements IQueryBuilder {
        /*
         * in this function i take the domain, city and keyword from the user and
         * generate the correct url and Query
         */
        public String getQuery(InputSearchAdDTO inputDto) {
            String city = inputDto.getCity();
            String domain = inputDto.getDomain();
            String keyword = inputDto.getKeyword();
            String state = inputDto.getState();
            String query = "{";
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
            query += "enabled: true }";
            return query;
        }

    }

    private BoApp() {
    }

    public static BoApp getInstance() {
        if (instance == null) {
            instance = new BoApp();
        }
        return instance;
    }

    /*
     * back Office Delete jobs
     */

    public void delete(String _id) {
        adService.delete(_id);
    }

    public void disable(String _id) {
        Ad ad = adService.get(_id);
        ad.setState(Ad.DISABLED);
        ad.setStateRank(Ad.DISABLED_RANK);
        adService.addOrUpdate(ad);
    }

    public AdResult getAds(int pageNumber, int numberOfAds) {
        return getAds(pageNumber, numberOfAds, BO_STATUS_FILTER_ALL);
    }

    /**
     * @param statusFilter valeur normalisée (voir {@link #normalizeBoStatusFilter(String)})
     */
    public AdResult getAds(int pageNumber, int numberOfAds, String statusFilter) {
        String normalized = normalizeBoStatusFilter(statusFilter);
        String queryFilter = BO_STATUS_FILTER_ALL.equals(normalized) ? null : normalized;
        List<AdDto> allAds = adService.getAllAds(pageNumber, numberOfAds, queryFilter);
        long count = adService.countForBoListing(queryFilter);
        return AdResult.builder()
                .ads(allAds)
                .totalAds(count)
                .build();
    }

    /**
     * Normalise le paramètre de filtre BO (query {@code filter}).
     */
    public static String normalizeBoStatusFilter(String raw) {
        if (raw == null || raw.isBlank()) {
            return BO_STATUS_FILTER_ALL;
        }
        String s = raw.trim().toLowerCase(Locale.ROOT);
        if (BO_STATUS_FILTER_NEW.equals(s)) {
            return BO_STATUS_FILTER_NEW;
        }
        if (BO_STATUS_FILTER_MODIFIED.equals(s)) {
            return BO_STATUS_FILTER_MODIFIED;
        }
        return BO_STATUS_FILTER_ALL;
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
