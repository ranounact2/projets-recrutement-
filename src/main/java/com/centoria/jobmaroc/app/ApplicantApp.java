package com.centoria.jobmaroc.app;

import com.centoria.jobmaroc.common.utils.MessageBundle;
import com.centoria.jobmaroc.dto.*;
import com.centoria.jobmaroc.dto.mapper.MapperApplicantDto;
import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.model.Applicant;
import com.centoria.jobmaroc.model.City;
import com.centoria.jobmaroc.model.Cv;
import com.centoria.jobmaroc.service.*;
import com.centoria.jobmaroc.service.impl.*;
import com.centoria.jobmaroc.service.MailServiceFactory;
import com.centoria.jobmaroc.service.IStorageService;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class ApplicantApp {
    private static final Logger logger = LoggerFactory.getLogger(ApplicantApp.class);
    private static ApplicantApp instance = null;
    private MapperApplicantDto mapperApplicant = Mappers.getMapper(MapperApplicantDto.class);
    private IApplicantService applicantService = ApplicantService.getInstance();
    private IMailService mailService = MailServiceFactory.getInstance();
    private IAdService adService = AdService.getInstance();
    private ICvService cvService = CvService.getInstance();
    private IStorageService fileStorageService = S3StorageService.getInstance();
    private ICityService cityService = CityService.getInstance();
    private IDomainService domainService = DomainService.getInstance();
    private MessageBundle messageBundle = MessageBundle.getInstance();


    private ApplicantApp() {
        this(
                Mappers.getMapper(MapperApplicantDto.class),
                ApplicantService.getInstance(),
                MailServiceFactory.getInstance(),
                AdService.getInstance(),
                CvService.getInstance(),
                S3StorageService.getInstance(),
                CityService.getInstance(),
                DomainService.getInstance(),
                MessageBundle.getInstance()
        );
    }

    /**
     * Constructeur pour les tests unitaires (même package) : injection des dépendances.
     */
    ApplicantApp(
            MapperApplicantDto mapperApplicant,
            IApplicantService applicantService,
            IMailService mailService,
            IAdService adService,
            ICvService cvService,
            IStorageService fileStorageService,
            ICityService cityService,
            IDomainService domainService,
            MessageBundle messageBundle) {
        this.mapperApplicant = mapperApplicant;
        this.applicantService = applicantService;
        this.mailService = mailService;
        this.adService = adService;
        this.cvService = cvService;
        this.fileStorageService = fileStorageService;
        this.cityService = cityService;
        this.domainService = domainService;
        this.messageBundle = messageBundle;
    }

    public static ApplicantApp getInstance() {
        if (instance == null) {
            instance = new ApplicantApp();
        }
        return instance;
    }

    public ApplicantDto getByJobId(ApplicantDto applicantDto) {
        return applicantService.prepareApplicationForm(applicantDto.getInputKey());
    }

    public ApplicantDto add(ApplicantDto inputDto) {

        if (inputDto.getErrors() == null || inputDto.getErrors().isEmpty()) {

            ApplicantDto applicantDto = new ApplicantDto();

            String _id = inputDto.getJobId();

            if (_id != null) {
                if (applicantService.hasExistingApplicationForJob(inputDto.getEmail(), _id)) {
                    inputDto.getErrors().put("email", getMessage("validation.application.duplicate"));
                    return inputDto;
                }

                String host = inputDto.getSiteUrl();
                Applicant application = mapperApplicant.asEntity(inputDto);
                applicantService.initState(application);

                byte[] cvBytes = (inputDto.getCv() != null && inputDto.getCv().getCvFile() != null
                        && inputDto.getCv().getCvFile().length > 0)
                        ? inputDto.getCv().getCvFile()
                        : null;

                if (cvBytes != null) {
                    Cv embeddedCv = new Cv();
                    embeddedCv.setEmail(inputDto.getEmail());
                    embeddedCv.setEnabled(true);

                    try {
                        if (fileStorageService.isAvailable()) {
                            String storageKey = buildStorageKey(_id, inputDto.getNom(), inputDto.getPrenom());
                            String storedKey = fileStorageService.store(storageKey, cvBytes, "application/pdf");
                            if (storedKey != null) {
                                embeddedCv.setStorageKey(storedKey);
                                embeddedCv.setStorageUrl(fileStorageService.getUrl(storedKey));
                                logger.info("CV sauvegardé dans le stockage cloud — clé: {}", storedKey);
                            } else {
                                logger.warn("Stockage cloud échoué pour {} — métadonnées CV sans clé S3", inputDto.getEmail());
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Erreur lors du stockage cloud du CV (candidature enregistrée quand même): {}", e.getMessage(), e);
                    }

                    application.setCv(embeddedCv);
                    cvService.addOrUpdate(embeddedCv);
                } else {
                    logger.warn("Avertissement: CV manquant pour la candidature de {}", inputDto.getEmail());
                }

                applicantService.addOrUpdate(application);

                Ad ad = adService.get(_id);
                
                // Préparer et envoyer l'email (ne pas bloquer si l'email échoue)
                boolean emailSent = false;
                try {
                    MailDto<ApplicantDto> mdto = new MailDto<ApplicantDto>(host, inputDto, ad.getTitle());
                    applicantService.renderTemplate(mdto);

                    byte[] cvFile = cvBytes;
                    String cvname = null;
                    if (cvFile != null && cvFile.length > 0) {
                        cvname = inputDto.getNom() + "-" + inputDto.getPrenom() + ".pdf";
                    }
                    mailService.sendMail(ad.getEmail(), null, mdto.getSubject(), mdto.getMailContent(), cvFile, cvname);
                    emailSent = true;
                } catch (Exception e) {
                    // Log l'erreur mais ne bloque pas l'enregistrement de la candidature
                    logger.error("Erreur lors de l'envoi de l'email (candidature enregistrée): {}", e.getMessage(), e);
                    // Informer l'utilisateur que la candidature est enregistrée mais l'email n'a pas pu être envoyé
                    applicantDto.getMessages().put("warning", getMessage("warning.application.saved.email.failed"));
                }

                // Message de succès conditionnel selon si l'email a été envoyé ou non
                if (emailSent) {
                    applicantDto.getMessages().put("message", getMessage("success.application.sent"));
                } else {
                    applicantDto.getMessages().put("message", getMessage("success.application.saved.email.failed"));
                }

                return applicantDto;

            } else {
                applicantDto.getErrors().put("message", getMessage("error.something.wrong"));
            }

        }
        return inputDto;

    }

    public SearchResultAdDto getReferenceData() {
        SearchResultAdDto result = new SearchResultAdDto();
        List<City> allCities = cityService.getAllCities();
        List<DomainDto> domainDtos = domainService.getAllDomain();
        result.setCities(allCities);
        result.setDomains(domainDtos);
        return result;
    }
    
    /**
     * Construit la clé S3 pour un CV : candidatures/{jobId}/{nom}-{prenom}-{uuid}.pdf
     */
    private String buildStorageKey(String jobId, String nom, String prenom) {
        String safeName = sanitize(nom) + "-" + sanitize(prenom);
        return "candidatures/" + sanitize(jobId) + "/" + safeName + "-" + UUID.randomUUID() + ".pdf";
    }

    private String sanitize(String value) {
        if (value == null) return "inconnu";
        return value.trim().toLowerCase().replaceAll("[^a-z0-9_-]", "_");
    }

    /**
     * Gets a localized message using MessageBundle singleton
     */
    private String getMessage(String key) {
        return messageBundle.getMessage(key);
    }

}
