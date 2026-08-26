package com.centoria.jobmaroc.service.impl;

import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.dao.IApplicantDao;
import com.centoria.jobmaroc.dao.impl.ApplicantDao;
import com.centoria.jobmaroc.dto.ApplicantDto;
import com.centoria.jobmaroc.dto.MailDto;
import com.centoria.jobmaroc.dto.mapper.MapperAdDto;
import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.model.Applicant;
import com.centoria.jobmaroc.service.IAdService;
import com.centoria.jobmaroc.service.IApplicantService;
import com.centoria.jobmaroc.web.base.FreeMarkerEngine;
import org.mapstruct.factory.Mappers;

public class ApplicantService extends BaseService<Applicant, IApplicantDao> implements IApplicantService {


    private static IApplicantService instance = null;

    FreeMarkerEngine template = new FreeMarkerEngine();
    private final IAdService adService = AdService.getInstance();
    private final MapperAdDto mapperAdDto = Mappers.getMapper(MapperAdDto.class);

    private ApplicantService() {
        this(ApplicantDao.getInstance());
    }

    /**
     * Pour les tests : injecte un DAO (ex. mock) sans toucher au singleton production.
     */
    public ApplicantService(IApplicantDao applicantDao) {
        this.dao = applicantDao;
    }

    public static IApplicantService getInstance() {
        if (instance == null) {
            instance = new ApplicantService();
        }
        return instance;
    }

    @Override
    public void initState(Applicant appli) {
        appli.setEnabled(true);
        appli.setState(Applicant.NEW);
        appli.setStateRank(Applicant.NEW_RANK);
    }

    @Override
    public void renderTemplate(MailDto<ApplicantDto> mdto) {
//        Map<String, Object> map = new HashMap<>();
//
//        ApplicantDto applidto = (ApplicantDto) mdto.getObject();
//        map.put("applicant", applidto);
//        map.put("host", mdto.getHost());
//        map.put("postTitle", mdto.getTitle());
//        ModelAndView mv = new ModelAndView(map, "emails/applicant.ftl");
//        String content = template.render(mv);
//        String subject = "Nouvelle candidature";
//
//        mdto.setMailContent(content);
//        mdto.setSubject(subject);
    }

    @Override
    public ApplicantDto prepareApplicationForm(String jobKey) {
        if (jobKey == null || jobKey.isBlank()) {
            throw new BusinessException("JOB_ID_MISSING", "Identifiant d'offre manquant");
        }
        Ad ad = adService.get(jobKey);
        if (ad == null) {
            throw new BusinessException("JOB_NOT_FOUND", "Offre introuvable");
        }
        ApplicantDto applicantDto = new ApplicantDto();
        applicantDto.setWithOffers(false);
        applicantDto.setInputKey(jobKey);
        applicantDto.setJob(mapperAdDto.asDto(ad));
        return applicantDto;
    }

    @Override
    public boolean hasExistingApplicationForJob(String email, String jobId) {
        return dao.existsByJobIdAndEmailIgnoreCase(jobId, email);
    }

}
