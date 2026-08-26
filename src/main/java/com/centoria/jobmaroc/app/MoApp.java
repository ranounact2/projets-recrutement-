package com.centoria.jobmaroc.app;

import com.centoria.jobmaroc.common.context.ApplicationContext;
import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.common.utils.MessageBundle;
import com.centoria.jobmaroc.dto.*;
import com.centoria.jobmaroc.dto.mapper.MapperAdDto;
import com.centoria.jobmaroc.dto.mapper.MapperDomainDto;
import com.centoria.jobmaroc.dao.IContactDao;
import com.centoria.jobmaroc.dao.impl.ContactDao;
import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.model.City;
import com.centoria.jobmaroc.model.Contact;
import com.centoria.jobmaroc.service.IAdService;
import com.centoria.jobmaroc.service.ICityService;
import com.centoria.jobmaroc.service.IDomainService;
import com.centoria.jobmaroc.service.IMailService;
import com.centoria.jobmaroc.service.impl.AdService;
import com.centoria.jobmaroc.service.impl.CityService;
import com.centoria.jobmaroc.service.impl.DomainService;
import com.centoria.jobmaroc.service.MailServiceFactory;
import com.centoria.jobmaroc.web.base.FreeMarkerEngine;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.codec.digest.DigestUtils;
import org.mapstruct.factory.Mappers;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class MoApp extends BaseApp {

    private static MoApp instance = null;
    MapperAdDto mapper = Mappers.getMapper(MapperAdDto.class);
    private MapperDomainDto mapperDomain = Mappers.getMapper(MapperDomainDto.class);
    FreeMarkerEngine template = new FreeMarkerEngine();
    private IAdService adService = AdService.getInstance();
    private ICityService cityService = CityService.getInstance();
    private IDomainService domainService = DomainService.getInstance();
    private IMailService mailService = MailServiceFactory.getInstance();
    private IContactDao contactDao = ContactDao.getInstance();
    private IQueryBuilder queryBuilder = new MoAppQueryBuilder();
    private MapperAdDto mapperJob = Mappers.getMapper(MapperAdDto.class);
    private MessageBundle messageBundle = MessageBundle.getInstance();

    /**
     * Récupère les annonces par secretCode en gérant les collisions MD5.
     * 
     * Cette méthode utilise une aggregation MongoDB pour:
     * 1. Grouper les annonces par email normalisé
     * 2. Sélectionner le groupe d'email le plus récent (basé sur max(creationDate, updateDate))
     * 3. Trier et paginer en base de données
     * 
     * AVANT: Le code chargeait TOUTES les annonces en mémoire (Integer.MAX_VALUE),
     *        puis faisait le regroupement, tri et pagination en Java.
     * 
     * MAINTENANT: Tout est fait en base de données via aggregation MongoDB,
     *              ce qui est beaucoup plus performant et scalable.
     * 
     * @param secretCode Le code secret MD5 de l'email
     * @param pageNumber Numéro de page (commence à 1)
     * @param numberOfAds Nombre d'annonces par page
     * @return AdResult contenant les annonces paginées et le total
     */
    public AdResult getAdsBySecretCode(String secretCode, int pageNumber, int numberOfAds) {
        // Utiliser la nouvelle méthode qui fait tout en base de données
        List<AdDto> ads = adService.getAdsBySecretCodeGroupedByEmail(secretCode, pageNumber, numberOfAds);
        long totalAds = adService.countAdsBySecretCodeGroupedByEmail(secretCode);
        
        return AdResult.builder()
                .ads(ads != null ? ads : new ArrayList<>())
                .totalAds(totalAds)
                .build();
    }

    /**
     * Récupère une annonce par son ID.
     * 
     * @param adId L'identifiant de l'annonce
     * @return L'annonce trouvée, ou null si non trouvée
     * @throws BusinessException Si erreur métier
     * @throws TechnicalException Si erreur technique
     */
    public Ad getAdById(String adId) throws BusinessException, TechnicalException {
        return adService.get(adId);
    }

    /*
     * in this function i take the domain, city and keyword from the user and
     * generate the correct url and Query
     */
    private class MoAppQueryBuilder implements IQueryBuilder {

        public String getQuery(InputSearchAdDTO inputDto) {
            String city = inputDto.getCity();
            String domain = inputDto.getDomain();
            String keyword = inputDto.getKeyword();
            String state = inputDto.getState();

            String url = null;
            String query = null;

            /*
             * get all jobs secret Code
             */
            if (inputDto.getSecretCode() != null) {
                query = "{secretCode: '" + inputDto.getSecretCode() + "',enabled: true}";
            } else {
                query = "{enabled: true}";
            }

            if (keyword != null) {
                query = "{$text:{$search:'" + keyword + "'}";
                if (inputDto.getSecretCode() != null) {
                    query += "',secretCode: '" + inputDto.getSecretCode();
                }

                query += "',enabled: true}";
            }


            return query;
        }
    }

    private MoApp() {

    }
    
    /**
     * Gets a localized message using MessageBundle singleton
     */
    private String getMessage(String key) {
        return messageBundle.getMessage(key);
    }

    public static MoApp getInstance() {
        if (instance == null) {
            instance = new MoApp();
        }
        return instance;
    }


    /*
     * MO delete jobs
     */
    public void delete(String _id, String secretCode) {
        if (secretCode.equals(adService.get(_id).getSecretCode())) {
            adService.delete(_id);
        }
    }

    /*
     * MO close Ad ==> enable=false
     */
    public void closeAd(String _id, String secretCode) {
        if (secretCode.equals(adService.get(_id).getSecretCode())) {
            Ad ad = adService.get(_id);
            if (ad == null) {
                throw new BusinessException("Ad not found");
            } else {
                ad.setEnabled(false);
                ad.setState(Ad.DISABLED);
                adService.addOrUpdate(ad);
            }

        }
    }

    /**
     * Retrieves advertisement data with reference data (cities, domains).
     * Handles two scenarios:
     * 1. If inputDto.getId() is provided → fetch Ad and return with reference data (update)
     * 2. If no ID is provided → return only reference data (creation or reference only)
     * 
     * The mapper converts Ad (entity) to AdDto (DTO) for the presentation layer.
     * 
     * @param inputDto The input DTO containing ID and options for reference data
     * @return AdWithReferenceDataDto containing job data (if ID provided) and reference data
     * @throws BusinessException If business error
     * @throws TechnicalException If technical error
     */
    public AdWithReferenceDataDto getWithReferenceData(InputSearchAdDTO inputDto) 
            throws BusinessException, TechnicalException {
        
        AdDto jdto = null;
        
        // If ID is provided, fetch the Ad and convert to DTO
        if (inputDto != null && inputDto.getId() != null && !inputDto.getId().isEmpty()) {
            Ad adEntity = adService.get(inputDto.getId());
            if (adEntity == null) {
                return null;
            }
            // Use mapper to convert Entity → DTO (separation of concerns)
            jdto = mapper.asDto(adEntity);
            // Note: Keep city as slug - the template compares with city.slug for selection
        }
        // If no ID → jdto remains null (creation or reference data only)
        // @Builder.Default will create empty AdDto automatically
        
        // Load reference data based on inputDto options
        List<City> cities = null;
        if (inputDto != null && inputDto.isWithCities()) {
            cities = cityService.get(null, null, CityService.SORT_BY_NAME_ASC, -1, -1);
        }
        
        List<DomainDto> domains = null;
        if (inputDto != null && (inputDto.isWithCategories() || inputDto.isWithDomainLinksForCity())) {
            domains = mapperDomain.asDtos(domainService.get(null, null, null, -1, -1));
        }

        // Build and return result
        AdWithReferenceDataDto.AdWithReferenceDataDtoBuilder builder = AdWithReferenceDataDto.builder()
                .cities(cities)
                .domains(domains);

        if (jdto != null) {
            builder.job(jdto);
        }
        // If jdto is null, @Builder.Default will create empty AdDto automatically

        return builder.build();
    }

    // Add or modify an offer and send an email to the user
    public AdWithReferenceDataDto addOrUpdate(AdWithReferenceDataDto jobdto) {
        jobdto.setCities(cityService.get(null, null, CityService.SORT_BY_NAME_ASC, -1, -1));
        jobdto.setDomains(mapperDomain.asDtos(domainService.get(null, null, null, -1, -1)));
        Map<String, String> validationErrors = jobdto.validate();

        if (validationErrors.isEmpty() || validationErrors == null) {
            String host = jobdto.getSiteUrl();
            final Ad ad = mapperJob.asEntity(jobdto.getJob());
            ad.setAnnouncetype(Ad.ANNOUNCE_TYPE_NORMAL);
            String _id = jobdto.getJob().getKey();
            if (_id != null) {
                // ou cas d'update

                Ad oldoffer = adService.get(_id);
                // for security check if the old email and annonce type is the same in the new
                // object
                if (oldoffer.getEmail().equals(ad.getEmail()) && oldoffer.getAnnouncetype() == ad.getAnnouncetype()) {

                    // verify if have image
                    if (ad.getImg() == null) {
                        ad.setImg(oldoffer.getImg());
                    } else {
                        ad.setImg(jobdto.getJob().getImg());
                    }
                    ad.setCreationDate(oldoffer.getCreationDate());
                    ad.setCode(oldoffer.getCode());
                    ad.setEnabled(true);
                    ad.setState(Ad.UPDATED_VERIFIED);
                    ad.setStateRank(Ad.UPDATED_VERIFIED_RANK);
                    adService.addOrUpdate(ad);
                    // Update the DTO with the updated ad data
                    jobdto.setJob(mapperJob.asDto(ad));

                    /*
                     * Notify admin (same pattern as creation)
                     */
                    String mailToUpdate = ApplicationContext.getInstance().getProps().getValue("mail.to");
                    String mailCcUpdate = ApplicationContext.getInstance().getProps().getValue("mail.cc");
                    String textUpdate = getMessage("info.ad.updated.notify");
                    String objectUpdate = getMessage("info.email.subject");

                    CompletableFuture<Void> mailFutureAdminUpdate = CompletableFuture.runAsync(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mailService.sendMail(mailToUpdate, mailCcUpdate, objectUpdate, textUpdate, null, null);
                            } catch (TechnicalException | BusinessException e) {
                                log.error("Error sending notification email to admin for ad update: {}", ad.getKey(), e);
                            }
                        }
                    });

                    /*
                     * Send confirmation email to the user who updated the ad
                     */
                    CompletableFuture<Void> mailFutureToUserUpdate = CompletableFuture.runAsync(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Map<String, Object> model = new HashMap<>();
                                AdDto jobDto = mapperJob.asDto(ad);
                                model.put("job", jobDto);
                                model.put("host", host);

                                Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
                                cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "/");
                                cfg.setDefaultEncoding("UTF-8");

                                Template template = cfg.getTemplate("emails/confirmation-modification.ftl");
                                StringWriter sw = new StringWriter();
                                template.process(model, sw);
                                String content = sw.toString();

                                String subject = getMessage("email.confirmation.modification.subject");
                                if (subject == null || subject.trim().isEmpty()) {
                                    subject = "Confirmation de modification d'annonce - Emplois Maroc";
                                }
                                mailService.sendMail(ad.getEmail(), null, subject, content, null, null);
                            } catch (TechnicalException | BusinessException | IOException | TemplateException e) {
                                log.error("Error sending confirmation email to user for ad update: {}", ad.getKey(), e);
                            }
                        }
                    });

                    jobdto.getMessages().put("message", getMessage("success.ad.updated"));

                } else {
                    jobdto.getErrors().put("message", getMessage("error.something.wrong"));
                }

            } else {
                // ou cas d'ajout
                // check if the annoounce Type equal 3 The default value
                if (ad.getAnnouncetype() == Ad.ANNOUNCE_TYPE_NORMAL) {
                    ad.setEnabled(true);
                    ad.setState(Ad.NEW);
                    ad.setStateRank(Ad.NEW_RANK);
                    if (ad.getEmail().equals("rtsystems.contact@gmail.com")) {
                        ad.setAnnouncetype(Ad.ANNOUNCE_TYPE_STAR);
                    }
                    adService.addOrUpdate(ad);
                    // Update the DTO with the created ad data (key, secretCode, etc.)
                    jobdto.setJob(mapperJob.asDto(ad));

                    /*
                     *Send email to supper admin
                     */

                    String emailTemplate = "creation";

                    CompletableFuture<Void> mailFutureToEmployer = CompletableFuture.runAsync(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sendEmail(ad, host, emailTemplate);
                            } catch (TechnicalException e) {
                                log.error("Error sending email for ad creation: {}", ad.getKey(), e);
                            } catch (BusinessException e) {
                                log.error("Business error sending email for ad creation: {}", ad.getKey(), e);
                            }
                        }
                    });

                    String mailTo = ApplicationContext.getInstance().getProps().getValue("mail.to");
                    String mailCc = ApplicationContext.getInstance().getProps().getValue("mail.cc");
                    String text = getMessage("info.new.ad.created");
                    String object = getMessage("info.email.subject");

                    /*
                     * Revoir une gestion globale des traitement asynchrone Notamment des actions
                     * ayant échoués
                     *
                     */
                    CompletableFuture<Void> mailFuture = CompletableFuture.runAsync(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mailService.sendMail(mailTo, mailCc, object, text, null, null);
                            } catch (TechnicalException | BusinessException e) {
                                log.error("Error sending notification email to admin for new ad", e);
                            }
                        }
                    });

                    /*
                     * Send confirmation email to the user who created the ad
                     */
                    CompletableFuture<Void> mailFutureToUser = CompletableFuture.runAsync(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Prepare model for email template
                                Map<String, Object> model = new HashMap<>();
                                AdDto jobDto = mapperJob.asDto(ad);
                                model.put("job", jobDto);
                                model.put("host", host);

                                // Configure FreeMarker
                                Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
                                cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "/");
                                cfg.setDefaultEncoding("UTF-8");

                                // Generate email content from template
                                Template template = cfg.getTemplate("emails/confirmation-creation.ftl");
                                StringWriter sw = new StringWriter();
                                template.process(model, sw);
                                String content = sw.toString();

                                // Send confirmation email to user
                                String subject = getMessage("email.confirmation.subject");
                                if (subject == null || subject.trim().isEmpty()) {
                                    subject = "Confirmation de création d'annonce - Emplois Maroc";
                                }
                                mailService.sendMail(ad.getEmail(), null, subject, content, null, null);
                            } catch (TechnicalException | BusinessException | IOException | TemplateException e) {
                                log.error("Error sending confirmation email to user for ad creation: {}", ad.getKey(), e);
                            }
                        }
                    });

                    // view hbs and message
                    jobdto.getMessages().put("message", getMessage("success.ad.added"));
                } else {
                    jobdto.getErrors().put("message", getMessage("error.something.wrong"));
                }
            }
        }

        return jobdto;
    }

    /**
     * Retrieves an advertisement by ID and its similar offers.
     *
     * @param jobDto The data transfer object containing the input key for the search.
     * @return An AdDisplayDto containing the advertisement and its similar offers.
     */
    public AdDisplayDto getById(AdDisplayDto jobDto) {
        AdDisplayDto result = new AdDisplayDto();
        Ad ad = adService.get(jobDto.getInputKey());
        AdDto jdto = mapperJob.asDto(ad);
        result.setJob(jdto);
        result.setSimilarOffre(adService.getSimilarAds(jobDto.getInputKey(), jdto.getDomain()));
        //result.getSimilarOffre().forEach(elem -> System.out.println(elem.getDomain()));
        return result;
    }

    // TODO: Ce code est conservé temporairement et sera modifié ou remplacé ultérieurement.
// Il permet actuellement d'envoyer un lien contenant un code secret à l'utilisateur
// pour accéder à ses annonces postées, mais devra être refactoré pour améliorer la logique et la sécurité.
//    public SearchResultAdDto sendEmailWithSecretCode(InputSearchAdDTO inputSearchAdDto, String host) {
//        SearchResultAdDto sDto = new SearchResultAdDto();
//        if (inputSearchAdDto.getEmail() != null) {
//            try {
//                String email = inputSearchAdDto.getEmail();
//                String secretCode = DigestUtils.md5Hex(email.toLowerCase().replaceAll(" ", ""));
//                Map<String, Object> map = new HashMap<>();
//                URL url = new URL(host);
//                String baseUrl = url.getProtocol() + "://" + url.getHost() + (url.getPort() != -1 ? ":" + url.getPort() : "");
//                map.put("secretCode", secretCode);
//                map.put("host", host);
//                map.put("url", baseUrl);
//                ModelAndView mv = new ModelAndView(map, "emails/jobslink.ftl");
//                String content = template.render(mv);
//                /*
//                 * Check
//                 */
//                String query = "{email:" + "'" + email + "'}";
//                long nbAdsForEmail = adService.count(query);
//
//                if (nbAdsForEmail > 0) {
//                    mailService.sendMail(inputSearchAdDto.getEmail(), null,
//                            "Votre lien vers vos annonces sur Emplois-maroc", content, null, null);
//                } else {
//                    sDto.getMessages().put("message", "Si vous aves des annonces postées sur le site, vous les trouverez dans le mail transmit");
//                }
//            } catch (TechnicalException | BusinessException e) {
//                log.error("Error in sendEmailWithSecretCode", e);
//                Map<String, String> messages = new HashMap<>();
//                sDto.setMessages(messages);
//                sDto.getMessages().put("message", "Une erreur est  survenue, veuillez contacter l'admin du site");
//            } catch (MalformedURLException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        return sDto;
//    }
//
    public ContactDto sendContactMail(ContactDto contactDto) {
        if (contactDto.getErrors() == null || contactDto.getErrors().isEmpty()) {
            String mailTo = ApplicationContext.getInstance().getProps().getValue("mail.to");
            if (mailTo == null || mailTo.trim().isEmpty()) {
                throw new TechnicalException("Configuration error: mail.to is not configured", "");
            }
            
            Map<String, Object> model = new HashMap<>();
            model.put("senderName", contactDto.getName() != null ? contactDto.getName() : "");
            model.put("email", contactDto.getEmail() != null ? contactDto.getEmail() : "");
            model.put("message", contactDto.getMessage() != null ? contactDto.getMessage() : "");
            model.put("phone", contactDto.getPhone() != null ? contactDto.getPhone() : "");
            model.put("host", contactDto.getSiteUrl() != null ? contactDto.getSiteUrl() : "");

            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "/");
            cfg.setDefaultEncoding("UTF-8");

            String content;
            try {
                Template t = cfg.getTemplate("emails/contact.ftl");
                try (StringWriter out = new StringWriter()) {
                    t.process(model, out);
                    content = out.toString();
                }
            } catch (IOException | TemplateException e) {
                String errorMsg = getMessage("error.mail.generation");
                if (errorMsg == null || errorMsg.trim().isEmpty()) {
                    errorMsg = "Erreur lors de la génération de l'email";
                }
                throw new TechnicalException(errorMsg, e.getMessage());
            }

            // Sauvegarde en base de données (critique - laisser l'exception remonter si échec)
            Contact contact = new Contact();
            contact.setName(contactDto.getName());
            contact.setEmail(contactDto.getEmail());
            contact.setPhone(contactDto.getPhone());
            contact.setObjet(contactDto.getObjet());
            contact.setMessage(contactDto.getMessage());
            contact.setCreationDate(Instant.now());
            contact.setEnabled(true);
            contactDao.addOrUpdate(contact);  // Si échec, TechnicalException remonte automatiquement

            // Envoi du mail (ne pas bloquer si l'email échoue)
            boolean emailSent = false;
            try {
                String mailCc = ApplicationContext.getInstance().getProps().getValue("mail.cc");
                String objet = contactDto.getObjet() != null ? contactDto.getObjet() : "Contact depuis le site";
                mailService.sendMail(mailTo, mailCc, objet, content, null, null);
                emailSent = true;
            } catch (TechnicalException | BusinessException e) {
                // Ne bloque pas car le message est déjà sauvegardé
            }

            // Message conditionnel selon si l'email a été envoyé ou non
            ContactDto cDto = new ContactDto();
            if (cDto.getMessages() == null) {
                cDto.setMessages(new HashMap<>());
            }
            if (emailSent) {
                String successMessage = getMessage("success.contact.sent");
                if (successMessage == null || successMessage.trim().isEmpty()) {
                    successMessage = "Votre message a bien été envoyé";
                }
                cDto.getMessages().put("messages", successMessage);
            } else {
                String warningMessage = getMessage("success.contact.saved.email.failed");
                if (warningMessage == null || warningMessage.trim().isEmpty()) {
                    warningMessage = "Votre message a été enregistré avec succès. Cependant, nous n'avons pas pu envoyer l'email de notification. Votre message sera traité manuellement.";
                }
                cDto.getMessages().put("messages", warningMessage);
            }
            return cDto;
        }
        
        return contactDto;
    }
    public SearchResultAdDto sendEmailWithSecretCode(
            InputSearchAdDTO inputDto,
            String host,
            Configuration cfg
    ) {
        SearchResultAdDto sDto = new SearchResultAdDto();
        String email = inputDto.getEmail();

        if (email == null || email.isBlank()) {
            sDto.getMessages().put("message", getMessage("validation.email.required"));
            return sDto;
        }

        try {
            // Normaliser l'email pour la recherche
            String normalizedEmail = email.trim().toLowerCase();
            String escapedEmail = normalizedEmail.replaceAll("([\\\\\"'$])", "\\\\$1");
            String query = "{email:{$regex:'^" + escapedEmail + "$',$options:'i'}, enabled: true, state: { $nin: [\"" + Ad.DELETED + "\", \"" + Ad.DISABLED + "\"] } }";
            
            // Vérifier si des offres existent pour cet email
            long nbAds = adService.count(query);
            if (nbAds > 0) {
                // Récupérer le secretCode réellement stocké dans la première offre trouvée
                // Cela garantit la compatibilité avec les anciennes offres même si le secretCode
                // a été calculé différemment lors de la création
                List<Ad> adsFound = adService.get(query, null, "{creationDate: -1}", 1, 1);
                
                String actualSecretCode;
                if (!adsFound.isEmpty() && adsFound.get(0).getSecretCode() != null && !adsFound.get(0).getSecretCode().isEmpty()) {
                    // Utiliser le secretCode réellement stocké en DB
                    actualSecretCode = adsFound.get(0).getSecretCode();
                } else {
                    // Fallback: utiliser le secretCode calculé si aucun trouvé
                    actualSecretCode = DigestUtils.md5Hex(email.toLowerCase().replaceAll(" ", ""));
                }
                
                // Préparer le modèle pour le template email
                Map<String, Object> model = new HashMap<>();
                model.put("secretCode", actualSecretCode);
                model.put("host", host);

                URL url = new URL(host);
                String baseUrl = url.getProtocol()
                        + "://" + url.getHost()
                        + ((url.getPort() > 0 && url.getPort() != 80 && url.getPort() != 443)
                        ? ":" + url.getPort() : "");
                model.put("url", baseUrl);

                // Rendu template avec le secretCode réel
                Template tpl = cfg.getTemplate("emails/jobslink.ftl");
                StringWriter sw = new StringWriter();
                tpl.process(model, sw);
                String content = sw.toString();

                // Envoyer l'email avec le lien contenant le bon secretCode
                String emailToSend = email.trim();
                mailService.sendMail(
                        emailToSend, null,
                        getMessage("email.sentAdsLink"),
                        content, null, null
                );
                sDto.getMessages().put(
                        "message",
                        getMessage("success.email.link.sent")
                );
            } else {
                sDto.getMessages().put(
                        "message",
                        getMessage("email.no.ads.found")
                );
            }

        } catch (TemplateException | IOException e) {
            log.error("Error generating email template for secret code", e);
            sDto.getMessages().put(
                    "message",
                    getMessage("error.message.generation")
            );
        } catch (TechnicalException | BusinessException e) {
            log.error("Error sending email with secret code", e);
            sDto.getMessages().put(
                    "message",
                    getMessage("error.internal")
            );
        }

        return sDto;
    }

    /**
     * Verifies the status of an advertisement by its ID and code, then updates and saves modifications.
     *
     * @param _id  The ID of the advertisement to verify.
     * @param code The code to check against the advertisement's code.
     * @param host The host for constructing links (if needed).
     * @return A SearchResultAdDto containing the verification result and potential messages.
     */
    public SearchResultAdDto verify(String _id, String code, String host) {
        Ad ad = adService.get(_id);
        SearchResultAdDto searchResultAdDto = new SearchResultAdDto();

        if (code.equals(ad.getCode())) {
            if (ad.getState().equals(Ad.NEW)) {
                ad.setState(Ad.NEW_VERIFIED);
                ad.setStateRank(Ad.NEW_VERIFIED_RANK);
                adService.updateState(ad);
//				searchResultAdDto.getMapMessage().put("message",
//						"vous avez franchit la première étape. Un email vous sera envoyé dés validation.");
//				searchResultAdDto.getMapMessage().put("titre", "Vérification");

            } else if (ad.getState().equals(Ad.UPDATED)) {

                ad.setState(Ad.UPDATED_VERIFIED);
                ad.setStateRank(Ad.UPDATED_VERIFIED_RANK);
                ad.setUpdateDate(Instant.now());
                adService.updateState(ad);

//				searchResultAdDto.getMapMessage().put("message",
//						"Vous avez franchit la première étape. Un email vous sera envoyé dés validation.");
//				searchResultAdDto.getMapMessage().put("titre", "Vérification");

            } else if (ad.getState().equals(Ad.NEW_VERIFIED) || ad.getState().equals(Ad.UPDATED_VERIFIED)) {
//				searchResultAdDto.getMapMessage().put("message", "Vous avez déjà vérifié votre annonce.");
//				searchResultAdDto.getMapMessage().put("titre", "Vérification");
            } else {
//				searchResultAdDto.getMapMessage().put("message", "Quelque chose de mal c'est passé, essayez plus tard");
//				searchResultAdDto.getMapMessage().put("titre", "Vérification");
            }
        }
        return searchResultAdDto;
    }
}

