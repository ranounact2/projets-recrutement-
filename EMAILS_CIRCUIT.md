# Analyse des circuits d'email - Emplois Maroc

**Projet :** Emplois Maroc  
**Date :** 13 février 2025  
**Objectif :** Cartographier les flux d'email, identifier les points de défaillance et proposer des recommandations

---

## 0. Exigences métier – Destinataires des emails

| Événement | Destinataires attendus |
|-----------|-------------------------|
| **Création d'offre** | Créateur de l'emploi + Back office |
| **Modification d'offre** | Créateur de l'emploi + Back office |
| **Validation d'offre** | Créateur de l'offre |
| **Candidature à une offre** | Créateur de l'offre + Postulant (candidat) |

---

## 1. Vue d'ensemble de l'architecture email

### 1.1 Composants principaux

| Composant | Fichier | Rôle |
|-----------|---------|------|
| **IMailService** | `service/IMailService.java` | Interface d'envoi d'email |
| **MailService** | `service/impl/MailService.java` | Implémentation SMTP (Gmail) |
| **MailJetApi** | `service/impl/MailJetApi.java` | Implémentation Mailjet (non utilisée) |
| **Templates FreeMarker** | `resources/emails/*.ftl` | Modèles HTML des emails |

### 1.2 Protocole et configuration

- **Protocole :** SMTP (JavaMail)
- **Serveur par défaut :** smtp.gmail.com:587 (TLS)
- **Authentification :** Mot de passe d'application Gmail requis
- **Format :** HTML (charset UTF-8)
- **Pièces jointes :** Support PDF (CV candidature)

---

## 2. Inventaire des circuits d'email

### 2.1 Circuit 1 : Création d'une offre d'emploi

**Déclencheur :** Soumission du formulaire `/ajouter-offre-emploi`  
**Classe :** `MoApp.addOrUpdate()`  
**Destinataires exigés :** Créateur de l'emploi + Back office  
**Emails envoyés :** 3 (asynchrones via `CompletableFuture`)

| # | Destinataire | Rôle | Template | Contenu | Statut |
|---|--------------|------|----------|---------|--------|
| 1 | Recruteur (ad.getEmail()) | Créateur | `createjob.ftl` via `AdService.renderTemplate` | "Votre annonce est en cours de validation" | ❌ **CASSÉ** |
| 2 | Admin (mail.to, mail.cc) | Back office | Texte brut | `info.new.ad.created` | ✅ OK |
| 3 | Recruteur (ad.getEmail()) | Créateur | `confirmation-creation.ftl` | Lien vers mes annonces + code secret | ✅ OK |

**Problème critique :** `AdService.renderTemplate(mdto, "creation")` est **vide** (implémentation commentée). L'email #1 envoie un contenu **null** au créateur.

---

### 2.2 Circuit 2 : Modification d'une offre d'emploi

**Déclencheur :** Soumission du formulaire de mise à jour via Middle Office  
**Classe :** `MoApp.addOrUpdate()`  
**Destinataires exigés :** Créateur de l'emploi + Back office  
**Emails à envoyer :** 2 (asynchrones)

| # | Destinataire | Rôle | Template | Contenu | Statut |
|---|--------------|------|----------|---------|--------|
| 1 | Recruteur (ad.getEmail()) | Créateur | `updatejob.ftl` via `AdService.renderTemplate` | "Validation de votre modification" + lien | ❌ **CASSÉ** |
| 2 | Admin (mail.to, mail.cc) | Back office | À définir | Notification de modification d'annonce | ⚠️ **À IMPLÉMENTER** |

**Problème :** `AdService.renderTemplate(mdto, "modification")` est vide. **Manque :** email de notification au back office.

---

### 2.3 Circuit 3 : Validation d'une offre (Back Office)

**Déclencheur :** Action admin "Valider" dans le back-office  
**Classe :** `BoApp.validate()`  
**Destinataires exigés :** Créateur de l'offre uniquement  
**Email envoyé :** 1 (asynchrone)

| # | Destinataire | Rôle | Template | Contenu | Statut |
|---|--------------|------|----------|---------|--------|
| 1 | Recruteur (ad.getEmail()) | Créateur | `validateOffer.ftl` via `AdService.renderTemplate` | "Validation de votre annonce" | ❌ **CASSÉ** |

**Problème :** `AdService.renderTemplate(mdto, "validate")` est vide. **Note :** Le template `validateOffer.ftl` n'existe pas dans le projet.

---

### 2.4 Circuit 4 : Candidature à une offre

**Déclencheur :** Soumission du formulaire `/postuler-emploi/{id}`  
**Classe :** `ApplicantApp.add()`  
**Destinataires exigés :** Créateur de l'offre + Postulant (candidat)  
**Emails à envoyer :** 2 (synchrone, ne bloque pas l'enregistrement)

| # | Destinataire | Rôle | Template | Pièce jointe | Statut |
|---|--------------|------|----------|--------------|--------|
| 1 | Recruteur (ad.getEmail()) | Créateur | `applicant.ftl` via `ApplicantService.renderTemplate` | CV (PDF) | ❌ **CASSÉ** |
| 2 | Postulant (applicant.getEmail()) | Candidat | À définir (ex. `applicant-confirmation.ftl`) | — | ⚠️ **À IMPLÉMENTER** |

**Problème :** `ApplicantService.renderTemplate(mdto)` est **vide**. L'email au créateur est envoyé avec `subject` et `mailContent` à **null**. **Manque :** email de confirmation au postulant.

---

### 2.5 Circuit 5 : Formulaire de contact

**Déclencheur :** Soumission du formulaire `/contact` (avec reCAPTCHA)  
**Classe :** `MoApp.sendContactMail()`  
**Emails :** 1

| # | Destinataire | Rôle | Template | Contenu | Statut |
|---|--------------|------|----------|---------|--------|
| 1 | Admin (mail.to, mail.cc) | Back office | `emails/contact.ftl` | Nom, email, téléphone, message du visiteur | ✅ OK |

**Particularités :**
- Message sauvegardé en BDD avant envoi (résilience)
- Si l'email échoue, l'utilisateur est informé mais le message est conservé

---

### 2.6 Circuit 6 : Lien "Mes annonces" (code secret)

**Déclencheur :** Soumission de l'email sur `/mes-annonces-emploi`  
**Classe :** `MoApp.sendEmailWithSecretCode()`  
**Email envoyé :** 1

| # | Destinataire | Rôle | Template | Contenu | Statut |
|---|--------------|------|----------|---------|--------|
| 1 | Utilisateur (email saisi) | Créateur / Recruteur | `emails/jobslink.ftl` | Lien vers `/m-office/mes-annonces/{secretCode}` | ✅ OK |

**Particularités :**
- Vérification que des offres existent pour cet email
- Utilisation du `secretCode` réel stocké en BDD (compatibilité anciennes offres)

---

## 3. Configuration email

### 3.1 Propriétés requises (`config.*.properties`)

| Propriété | Description | Exemple |
|-----------|-------------|---------|
| `mail.from` | Adresse expéditrice | marocemploiss@gmail.com |
| `mail.from.pass` | Mot de passe d'application Gmail | (secret) |
| `mail.to` | Destinataire admin (contact, nouvelles annonces) | admin@example.com |
| `mail.cc` | Copie admin | admin@example.com |
| `mail.smtp.host` | Serveur SMTP | smtp.gmail.com |
| `mail.smtp.port` | Port SMTP | 587 |
| `mail.smtp.auth` | Authentification | true |
| `mail.smtp.starttls.enable` | TLS | true |
| `mail.debug` | Logs debug | false |

### 3.2 Mailjet (optionnel, non utilisé)

| Propriété | Description |
|-----------|-------------|
| `mailjet.apiKey` | Clé API Mailjet |
| `mailjet.secretKey` | Clé secrète Mailjet |

---

## 4. Templates email

| Fichier | Utilisé par | Variables |
|---------|-------------|-----------|
| `createjob.ftl` | Circuit 1 – Créateur (cassé) | `${job.title}` |
| `updatejob.ftl` | Circuit 2 – Créateur (cassé) | `${job.title}`, `${host}`, `${job.secretCode}` |
| *(à créer)* | Circuit 2 – Back office | Notification modification |
| `confirmation-creation.ftl` | Circuit 1 – Créateur (OK) | `${job.title}`, `${job.secretCode}`, `${host}` |
| `applicant.ftl` | Circuit 4 – Créateur (cassé) | `${postTitle}`, `${applicant.*}` |
| `applicant-confirmation.ftl` | Circuit 4 – Postulant (à créer) | Confirmation de candidature |
| `contact.ftl` | Circuit 5 (OK) | `${senderName}`, `${email}`, `${message}`, `${phone}` |
| `jobslink.ftl` | Circuit 6 (OK) | `${secretCode}`, `${host}`, `${url}` |
| `validateOffer.ftl` | Circuit 3 | **N'existe pas** |

---

## 5. Flux de données (schéma)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         DÉCLENCHEURS                                        │
├─────────────────────────────────────────────────────────────────────────────┤
│  AddJobOfferServlet  │  JobOfferUpdateServlet  │  BoApp.validate()         │
│  ApplicantApp.add()  │  ContactServlet         │  MyJobAdsServlet (POST)   │
└──────────┬───────────┴────────────┬────────────┴────────────┬──────────────┘
           │                        │                          │
           ▼                        ▼                          ▼
┌──────────────────┐    ┌──────────────────┐    ┌──────────────────────────┐
│ MoApp.addOrUpdate│    │ MoApp.sendContact│    │ MoApp.sendEmailWithSecret │
│ ApplicantApp.add │    │ Mail()           │    │ Code()                   │
└────────┬─────────┘    └────────┬─────────┘    └────────────┬──────────────┘
         │                       │                           │
         ▼                       ▼                           ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    RENDU TEMPLATE (FreeMarker)                              │
├─────────────────────────────────────────────────────────────────────────────┤
│  AdService.renderTemplate()      → ❌ VIDE (creation, modification, validate)│
│  ApplicantService.renderTemplate() → ❌ VIDE                                 │
│  MoApp (direct)                  → ✅ contact.ftl, confirmation-creation    │
│  MoApp (direct)                  → ✅ jobslink.ftl                          │
└─────────────────────────────────────────────────────────────────────────────┘
                                         │
                                         ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    MailService.sendMail()                                    │
│  - SMTP Gmail                                                                │
│  - Pièce jointe optionnelle (CV)                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 6. Problèmes identifiés

### 6.1 Critiques (bloquants)

| # | Problème | Impact |
|---|----------|--------|
| 1 | `AdService.renderTemplate()` vide | Emails création, modification, validation envoyés avec contenu null |
| 2 | `ApplicantService.renderTemplate()` vide | Email candidature envoyé avec subject/content null au créateur ; pas d'email au postulant |
| 3 | Template `validateOffer.ftl` absent | Circuit validation back-office non fonctionnel |

### 6.2 Moyens

| # | Problème | Impact |
|---|----------|--------|
| 4 | Pas de gestion des échecs asynchrones | Les `CompletableFuture` ne gèrent pas les échecs (pas de retry, pas d'alerte) |
| 5 | Credentials Gmail en clair | `mail.from.pass` dans config (risque sécurité) |
| 6 | Mailjet non utilisé | Code mort, dépendance inutile |
| 7 | Template `jobslink.ftl` trop lourd | Contient header/footer complet (HTML ~350 lignes) pour un simple lien |

### 6.3 Mineurs

| # | Problème | Impact |
|---|----------|--------|
| 8 | Pas de file d'attente | En cas de pic, risque de timeouts SMTP |
| 9 | Pas de logs structurés | Difficile de tracer les envois |
| 10 | Pas de test unitaire sur les emails | Régression possible |

---

## 7. Recommandations

### 7.1 Priorité 1 : Corriger les circuits cassés

**Action :** Implémenter `AdService.renderTemplate()` et `ApplicantService.renderTemplate()` avec FreeMarker.

**Exemple pour AdService :**

```java
@Override
public void renderTemplate(MailDto<AdDto> mdto, String emailTemplate) {
    AdDto jobdto = mdto.getObject();
    Map<String, Object> model = new HashMap<>();
    model.put("job", jobdto);
    model.put("host", mdto.getHost());

    Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
    cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "/");
    cfg.setDefaultEncoding("UTF-8");

    String templatePath;
    String subject;
    switch (emailTemplate) {
        case "creation":
            templatePath = "emails/createjob.ftl";
            subject = "Votre annonce " + jobdto.getTitle() + " est en cours de validation";
            break;
        case "modification":
            templatePath = "emails/updatejob.ftl";
            subject = "Validation de votre modification";
            break;
        case "validate":
            templatePath = "emails/validateOffer.ftl";
            subject = "Validation de votre annonce";
            break;
        default:
            throw new TechnicalException("Template inconnu: " + emailTemplate, "");
    }
    // Rendu template + set mdto.setMailContent() et mdto.setSubject()
}
```

**Action :** Créer le template `emails/validateOffer.ftl` (sur le modèle de `updatejob.ftl`).

---

### 7.2 Priorité 2 : Sécurisation

| Recommandation | Action |
|----------------|--------|
| **Variables d'environnement** | Remplacer `mail.from.pass` par une variable d'environnement (ex: `MAIL_PASSWORD`) |
| **Secrets manager** | En production, utiliser un gestionnaire de secrets (Vault, AWS Secrets Manager) |
| **Validation des destinataires** | Vérifier le format email avant envoi |

---

### 7.3 Priorité 3 : Robustesse

| Recommandation | Action |
|----------------|--------|
| **File d'attente** | Introduire une queue (ex: RabbitMQ, Redis) pour découpler l'envoi |
| **Retry** | Implémenter des tentatives en cas d'échec SMTP (3 tentatives avec backoff) |
| **Monitoring** | Logger les envois (succès/échec) avec un identifiant de corrélation |
| **Fallback** | Si SMTP échoue, sauvegarder l'email en BDD pour envoi différé |

---

### 7.4 Priorité 4 : Maintenance

| Recommandation | Action |
|----------------|--------|
| **Alléger jobslink.ftl** | Créer une version minimaliste (sans header/footer complet) |
| **Tests** | Ajouter des tests unitaires mockant `IMailService` |
| **Documentation** | Documenter chaque circuit dans ce fichier lors des évolutions |
| **Supprimer Mailjet** | Retirer `MailJetApi` si non utilisé |

---

### 7.5 Priorité 5 : Évolutions futures

| Recommandation | Description |
|----------------|-------------|
| **Stockage CV cloud** | Aligner avec les tâches #7234/#7128 (OVH/AWS S3) – envoyer un lien au lieu du CV en pièce jointe |
| **Emails transactionnels** | Envisager SendGrid, Mailjet ou Amazon SES pour la production |
| **Templates responsive** | Utiliser des frameworks email (MJML, Foundation for Emails) |
| **Double opt-in** | Pour "Mes annonces", envoyer un lien de confirmation avant le lien final |

---

## 8. Checklist de vérification

- [ ] Implémenter `AdService.renderTemplate()` (creation, modification, validate)
- [ ] Implémenter `ApplicantService.renderTemplate()`
- [ ] Créer `emails/validateOffer.ftl`
- [ ] Ajouter email Back office pour la modification d'offre (Circuit 2)
- [ ] Ajouter email de confirmation au postulant pour la candidature (Circuit 4)
- [ ] Tester manuellement chaque circuit
- [ ] Externaliser les secrets (mail.from.pass)
- [ ] Ajouter des logs structurés pour les envois
- [ ] Documenter la configuration dans CONFIGURATION_GUIDE.md

---

## 9. Références

| Document | Contenu |
|----------|---------|
| `CONFIGURATION_GUIDE.md` | Configuration par environnement |
| `config.sample.properties` | Template de configuration |
| `CONCEPTION.md` | UC-005 (Postuler), UC-006 (Publier offre), UC-008 (Contact) |

---

*Document généré à partir de l'analyse du code source Emplois Maroc.*
