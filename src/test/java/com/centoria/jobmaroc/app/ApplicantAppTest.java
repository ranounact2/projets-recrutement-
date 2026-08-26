package com.centoria.jobmaroc.app;

import com.centoria.jobmaroc.common.utils.MessageBundle;
import com.centoria.jobmaroc.dao.IAdDao;
import com.centoria.jobmaroc.dao.IApplicantDao;
import com.centoria.jobmaroc.dao.ICvDao;
import com.centoria.jobmaroc.dto.ApplicantDto;
import com.centoria.jobmaroc.dto.mapper.MapperApplicantDto;
import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.model.Applicant;
import com.centoria.jobmaroc.model.Cv;
import com.centoria.jobmaroc.service.IMailService;
import com.centoria.jobmaroc.service.IStorageService;
import com.centoria.jobmaroc.service.impl.AdService;
import com.centoria.jobmaroc.service.impl.ApplicantService;
import com.centoria.jobmaroc.service.impl.CityService;
import com.centoria.jobmaroc.service.impl.CvService;
import com.centoria.jobmaroc.service.impl.DomainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests unitaires de la couche applicative {@link ApplicantApp}.
 * <p>
 * Cas nominal (non-régression) et cas d'erreur / règles de contrôle :
 * <ul>
 *   <li>validation déjà en échec (une ou plusieurs erreurs) → pas de persistance ;</li>
 *   <li>identifiant d'offre manquant → pas de persistance (le DTO retourné est l'entrée) ;</li>
 *   <li>candidature valide sans ou avec CV → persistance + mail ;</li>
 *   <li>fichier CV vide → traité comme absence de CV (pas de sous-document {@code cv}) ;</li>
 *   <li>stockage objet disponible → upload puis même {@code Cv} enregistré dans {@code applicant} et collection {@code cv} (sans binaire) ;</li>
 *   <li>upload S3 en échec ou retour null → métadonnées CV sans clé S3 aux deux endroits ;</li>
 *   <li>échec d'envoi mail → candidat enregistré quand même + message d'avertissement.</li>
 * </ul>
 * On mocke la frontière persistance (DAO) et les I/O externes (mail, stockage objet) ;
 * les services métier réels s'exécutent au-dessus des DAO mockés.
 */
@ExtendWith(MockitoExtension.class)
class ApplicantAppTest {

    @Mock
    private IApplicantDao applicantDao;
    @Mock
    private ICvDao cvDao;
    @Mock
    private IAdDao adDao;
    @Mock
    private IMailService mailService;
    @Mock
    private IStorageService fileStorageService;

    private ApplicantApp buildApplicantApp() {
        ApplicantService applicantService = new ApplicantService(applicantDao);
        CvService cvService = new CvService(cvDao);
        AdService adService = new AdService(adDao);
        return new ApplicantApp(
                Mappers.getMapper(MapperApplicantDto.class),
                applicantService,
                mailService,
                adService,
                cvService,
                fileStorageService,
                CityService.getInstance(),
                DomainService.getInstance(),
                MessageBundle.getInstance());
    }

    @Test
    void add_returnsInputDtoWithoutCallingServicesWhenValidationErrorsPresent() {
        ApplicantApp app = buildApplicantApp();

        ApplicantDto input = new ApplicantDto();
        input.getErrors().put("nom", "required");

        ApplicantDto result = app.add(input);

        assertSame(input, result);
        verifyNoInteractions(applicantDao, cvDao, adDao, mailService, fileStorageService);
    }

    @Test
    void add_returnsInputDtoWithoutCallingServicesWhenMultipleValidationErrorsPresent() {
        ApplicantApp app = buildApplicantApp();

        ApplicantDto input = new ApplicantDto();
        input.getErrors().put("nom", "required");
        input.getErrors().put("email", "invalid");

        ApplicantDto result = app.add(input);

        assertSame(input, result);
        verifyNoInteractions(applicantDao, cvDao, adDao, mailService, fileStorageService);
    }

    @Test
    void add_returnsInputDtoWhenJobIdMissing() {
        ApplicantApp app = buildApplicantApp();

        ApplicantDto input = new ApplicantDto();
        input.setNom("Dupont");
        input.setPrenom("Jean");
        input.setEmail("candidat@example.com");
        input.setJobId(null);

        ApplicantDto result = app.add(input);

        assertSame(input, result);
        verifyNoInteractions(applicantDao, cvDao, adDao, mailService, fileStorageService);
    }

    @Test
    void add_returnsInputDtoWithoutPersistingWhenAlreadyAppliedWithSameEmail() {
        String jobId = "jobDuplicateBlock";
        when(applicantDao.existsByJobIdAndEmailIgnoreCase(jobId, "already@example.com")).thenReturn(true);

        ApplicantApp app = buildApplicantApp();

        ApplicantDto input = new ApplicantDto();
        input.setJobId(jobId);
        input.setSiteUrl("http://localhost:8080");
        input.setNom("Dupont");
        input.setPrenom("Jean");
        input.setEmail("already@example.com");
        input.setPhone("0612345678");
        input.setMotivation("Motivation suffisamment longue.");
        input.setFormation("Master");
        input.setExperienceLevel("junior");

        ApplicantDto result = app.add(input);

        assertSame(input, result);
        assertTrue(result.getErrors().containsKey("email"));
        assertTrue(result.getErrors().get("email").contains("déjà"));
        verify(applicantDao).existsByJobIdAndEmailIgnoreCase(jobId, "already@example.com");
        verify(applicantDao, never()).addOrUpdate(any(Applicant.class));
        verifyNoInteractions(cvDao, adDao, mailService, fileStorageService);
    }

    @Test
    void add_persistsApplicantAndSendsMailWhenNoValidationErrorsAndNoCv() {
        String jobId = "jobKeyTest123";
        Ad ad = new Ad();
        ad.setTitle("Offre test");
        ad.setEmail("recruteur@example.com");
        when(adDao.find(jobId)).thenReturn(ad);

        ApplicantApp app = buildApplicantApp();

        ApplicantDto input = new ApplicantDto();
        input.setJobId(jobId);
        input.setSiteUrl("http://localhost:8080");
        input.setNom("Dupont");
        input.setPrenom("Jean");
        input.setEmail("candidat@example.com");
        input.setPhone("0612345678");
        input.setMotivation("Motivation suffisamment longue.");
        input.setFormation("Master");
        input.setExperienceLevel("junior");

        ApplicantDto result = app.add(input);

        assertNotSame(input, result);
        assertNotNull(result.getMessages().get("message"));
        verify(applicantDao).addOrUpdate(any(Applicant.class));
        verify(adDao).find(jobId);
        verify(mailService).sendMail(
                eq("recruteur@example.com"),
                isNull(),
                any(),
                any(),
                isNull(),
                isNull());
        verifyNoInteractions(cvDao);
        verifyNoInteractions(fileStorageService);
    }

    @Test
    void add_persistsApplicantCvAndSendsMailWithPdfAttachmentWhenCvProvided() {
        String jobId = "jobKeyWithCv456";
        Ad ad = new Ad();
        ad.setTitle("Offre avec CV");
        ad.setEmail("recruteur@example.com");
        when(adDao.find(jobId)).thenReturn(ad);
        when(fileStorageService.isAvailable()).thenReturn(false);

        ApplicantApp app = buildApplicantApp();

        byte[] pdfBytes = new byte[]{0x25, 0x50, 0x44, 0x46};
        Cv cv = new Cv("candidat@example.com", pdfBytes);

        ApplicantDto input = new ApplicantDto();
        input.setJobId(jobId);
        input.setSiteUrl("http://localhost:8080");
        input.setNom("Dupont");
        input.setPrenom("Jean");
        input.setEmail("candidat@example.com");
        input.setPhone("0612345678");
        input.setMotivation("Motivation suffisamment longue.");
        input.setFormation("Master");
        input.setExperienceLevel("junior");
        input.setCv(cv);

        ApplicantDto result = app.add(input);

        assertNotNull(result.getMessages().get("message"));
        ArgumentCaptor<Applicant> applicantCaptor = ArgumentCaptor.forClass(Applicant.class);
        verify(applicantDao).addOrUpdate(applicantCaptor.capture());
        Applicant persisted = applicantCaptor.getValue();
        assertNotNull(persisted.getCv());
        assertEquals("candidat@example.com", persisted.getCv().getEmail());
        assertNull(persisted.getCv().getStorageKey());
        verify(adDao).find(jobId);
        verify(mailService).sendMail(
                eq("recruteur@example.com"),
                isNull(),
                any(),
                any(),
                eq(pdfBytes),
                eq("Dupont-Jean.pdf"));
        verify(fileStorageService).isAvailable();
        verify(fileStorageService, never()).store(any(), any(), any());
        verify(cvDao).addOrUpdate(any(Cv.class));
    }

    @Test
    void add_persistsApplicantButRecordsWarningWhenMailSendFails() {
        String jobId = "jobKeyMailFail789";
        Ad ad = new Ad();
        ad.setTitle("Offre mail KO");
        ad.setEmail("recruteur@example.com");
        when(adDao.find(jobId)).thenReturn(ad);
        doThrow(new RuntimeException("SMTP indisponible"))
                .when(mailService)
                .sendMail(any(), any(), any(), any(), any(), any());

        ApplicantApp app = buildApplicantApp();

        ApplicantDto input = new ApplicantDto();
        input.setJobId(jobId);
        input.setSiteUrl("http://localhost:8080");
        input.setNom("Martin");
        input.setPrenom("Claire");
        input.setEmail("candidat2@example.com");
        input.setPhone("0612345678");
        input.setMotivation("Motivation suffisamment longue.");
        input.setFormation("Licence");
        input.setExperienceLevel("senior");

        ApplicantDto result = app.add(input);

        verify(applicantDao).addOrUpdate(any(Applicant.class));
        verifyNoInteractions(cvDao);
        assertNotNull(result.getMessages().get("warning"));
        assertNotNull(result.getMessages().get("message"));
        verify(mailService).sendMail(any(), any(), any(), any(), any(), any());
    }

    @Test
    void add_doesNotPersistCvWhenCvFileEmpty() {
        String jobId = "jobKeyEmptyCv";
        Ad ad = new Ad();
        ad.setTitle("Offre");
        ad.setEmail("recruteur@example.com");
        when(adDao.find(jobId)).thenReturn(ad);

        ApplicantApp app = buildApplicantApp();

        Cv cv = new Cv("candidat@example.com", new byte[0]);

        ApplicantDto input = new ApplicantDto();
        input.setJobId(jobId);
        input.setSiteUrl("http://localhost:8080");
        input.setNom("Dupont");
        input.setPrenom("Jean");
        input.setEmail("candidat@example.com");
        input.setPhone("0612345678");
        input.setMotivation("Motivation suffisamment longue.");
        input.setFormation("Master");
        input.setExperienceLevel("junior");
        input.setCv(cv);

        ApplicantDto result = app.add(input);

        assertNotNull(result.getMessages().get("message"));
        ArgumentCaptor<Applicant> emptyCvCaptor = ArgumentCaptor.forClass(Applicant.class);
        verify(applicantDao).addOrUpdate(emptyCvCaptor.capture());
        assertNull(emptyCvCaptor.getValue().getCv());
        verifyNoInteractions(cvDao);
        verifyNoInteractions(fileStorageService);
        verify(mailService).sendMail(
                eq("recruteur@example.com"),
                isNull(),
                any(),
                any(),
                isNull(),
                isNull());
    }

    @Test
    void add_uploadsCvToStorageWhenAvailableAndPersistsCvWithKeys() {
        String jobId = "jobS3Ok";
        Ad ad = new Ad();
        ad.setTitle("Offre S3");
        ad.setEmail("recruteur@example.com");
        when(adDao.find(jobId)).thenReturn(ad);

        String storedKey = "candidatures/jobs3/dupont-jean-uuid.pdf";
        String publicUrl = "https://bucket.example/" + storedKey;
        when(fileStorageService.isAvailable()).thenReturn(true);
        when(fileStorageService.store(anyString(), any(), eq("application/pdf"))).thenReturn(storedKey);
        when(fileStorageService.getUrl(storedKey)).thenReturn(publicUrl);

        ApplicantApp app = buildApplicantApp();

        byte[] pdfBytes = new byte[]{0x25, 0x50, 0x44, 0x46};
        Cv cv = new Cv("candidat@example.com", pdfBytes);

        ApplicantDto input = new ApplicantDto();
        input.setJobId(jobId);
        input.setSiteUrl("http://localhost:8080");
        input.setNom("Dupont");
        input.setPrenom("Jean");
        input.setEmail("candidat@example.com");
        input.setPhone("0612345678");
        input.setMotivation("Motivation suffisamment longue.");
        input.setFormation("Master");
        input.setExperienceLevel("junior");
        input.setCv(cv);

        app.add(input);

        verify(fileStorageService).store(
                argThat(key -> key != null && key.startsWith("candidatures/" + jobId.toLowerCase() + "/")),
                eq(pdfBytes),
                eq("application/pdf"));
        verify(fileStorageService).getUrl(storedKey);

        ArgumentCaptor<Applicant> applicantCaptor = ArgumentCaptor.forClass(Applicant.class);
        verify(applicantDao).addOrUpdate(applicantCaptor.capture());
        Cv embedded = applicantCaptor.getValue().getCv();
        assertNotNull(embedded);
        assertEquals(storedKey, embedded.getStorageKey());
        assertEquals(publicUrl, embedded.getStorageUrl());
        ArgumentCaptor<Cv> cvRowCaptor = ArgumentCaptor.forClass(Cv.class);
        verify(cvDao).addOrUpdate(cvRowCaptor.capture());
        assertEquals(storedKey, cvRowCaptor.getValue().getStorageKey());
        assertEquals(publicUrl, cvRowCaptor.getValue().getStorageUrl());
    }

    @Test
    void add_persistsCvInDbWhenStorageStoreReturnsNull() {
        String jobId = "jobS3Null";
        Ad ad = new Ad();
        ad.setTitle("Offre");
        ad.setEmail("recruteur@example.com");
        when(adDao.find(jobId)).thenReturn(ad);
        when(fileStorageService.isAvailable()).thenReturn(true);
        when(fileStorageService.store(anyString(), any(), eq("application/pdf"))).thenReturn(null);

        ApplicantApp app = buildApplicantApp();

        byte[] pdfBytes = new byte[]{0x25, 0x50, 0x44, 0x46};
        Cv cv = new Cv("x@y.com", pdfBytes);

        ApplicantDto input = new ApplicantDto();
        input.setJobId(jobId);
        input.setSiteUrl("http://localhost:8080");
        input.setNom("Nom");
        input.setPrenom("Prenom");
        input.setEmail("x@y.com");
        input.setPhone("0612345678");
        input.setMotivation("Motivation suffisamment longue.");
        input.setFormation("Master");
        input.setExperienceLevel("junior");
        input.setCv(cv);

        app.add(input);

        ArgumentCaptor<Applicant> applicantCaptor = ArgumentCaptor.forClass(Applicant.class);
        verify(applicantDao).addOrUpdate(applicantCaptor.capture());
        Cv embedded = applicantCaptor.getValue().getCv();
        assertNotNull(embedded);
        assertNull(embedded.getStorageKey());
        assertNull(embedded.getStorageUrl());
        verify(fileStorageService, never()).getUrl(anyString());
        verify(cvDao).addOrUpdate(any(Cv.class));
    }

    @Test
    void add_persistsCvInDbWhenStorageStoreThrows() {
        String jobId = "jobS3Throw";
        Ad ad = new Ad();
        ad.setTitle("Offre");
        ad.setEmail("recruteur@example.com");
        when(adDao.find(jobId)).thenReturn(ad);
        when(fileStorageService.isAvailable()).thenReturn(true);
        when(fileStorageService.store(anyString(), any(), eq("application/pdf")))
                .thenThrow(new RuntimeException("timeout S3"));

        ApplicantApp app = buildApplicantApp();

        byte[] pdfBytes = new byte[]{0x25, 0x50, 0x44, 0x46};
        Cv cv = new Cv("z@w.com", pdfBytes);

        ApplicantDto input = new ApplicantDto();
        input.setJobId(jobId);
        input.setSiteUrl("http://localhost:8080");
        input.setNom("A");
        input.setPrenom("B");
        input.setEmail("z@w.com");
        input.setPhone("0612345678");
        input.setMotivation("Motivation suffisamment longue.");
        input.setFormation("Master");
        input.setExperienceLevel("junior");
        input.setCv(cv);

        ApplicantDto result = app.add(input);

        ArgumentCaptor<Applicant> applicantCaptor = ArgumentCaptor.forClass(Applicant.class);
        verify(applicantDao).addOrUpdate(applicantCaptor.capture());
        assertNotNull(applicantCaptor.getValue().getCv());
        verify(cvDao).addOrUpdate(any(Cv.class));
        assertNotNull(result.getMessages().get("message"));
    }
}
