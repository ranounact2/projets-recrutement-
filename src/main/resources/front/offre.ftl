<!DOCTYPE html>
<html lang = "en">

<head>
    <#include "../common/head.ftl">

    <#-- Récupération des valeurs depuis les zones -->
    <#assign zoneH1 = "Ajouter une offre d'emploi">
    <#assign zoneH2 = "Offre d'emploi (service gratuit)">
    <#assign zoneTitle = "Ajouter une offre d'emploi - EmploiMaroc">
    <#assign zoneParagraph = "Prenez le temps pour préparer tous les éléments nécessaires...">

    <#if zones??>
        <#list zones as zonePage>
            <#if zonePage.zones??>
                <#list zonePage.zones?keys as zoneKey>
                    <#assign currentZone = zonePage.zones[zoneKey]>
                    <#if currentZone.values??>
                        <#if currentZone.values.h1??>
                            <#assign zoneH1 = currentZone.values.h1.content!currentZone.values.h1.value!''>
                        </#if>
                        <#if currentZone.values.h2??>
                            <#assign zoneH2 = currentZone.values.h2.content!currentZone.values.h2.value!''>
                        </#if>
                        <#if currentZone.values.title??>
                            <#assign zoneTitle = currentZone.values.title.content!currentZone.values.title.value!''>
                        </#if>
                        <#if currentZone.values.h1_block??>
                            <#assign zoneParagraph = currentZone.values.h1_block.content!currentZone.values.h1_block.value!''>
                        </#if>
                    </#if>
                </#list>
            </#if>
        </#list>
    </#if>
    <title>${zoneTitle}</title>
    <link rel="stylesheet" href="/assets/css-min/style.min.css" defer/>
    <link rel="stylesheet" href="/assets/css-min/temp/add-applicant.min.css" defer/>

</head>

<body>

    <header>
        <#include "../common/header.ftl">
    </header>

        <!-- Breadcumb Section 1 -->
        <section class="breadcumb">
            <#if !data.job?? || (data.job?? && !data.job.key??)>
                <h1>${zoneH1}</h1>
                <ul>
                    <li><a href="/">Accueil</a></li>
                    <li><a href="/ajouter-offre-emploi">Ajouter Une Offre D'emploi</a></li>
                </ul>
            </#if>
        </section>

        <!-- Breadcumb Section 2 -->
    <#if secretCode??>
        <section class="breadcumb">

            <h1>${zoneH1}</h1>
        <ul>
            <li><a href="/">Accueil</a></li>
            <li><a href="/m-office/mo-annonce/${secretCode}">Voir mes offres</a></li>
        </ul>

        </section>
    </#if>
    <#if data?? && data.job?? && data.job.secretCode??>
        </#if>
        <section class="condidat">
            <div class="box-br">
                <h2>${zoneH2}</h2>
                <p>${zoneParagraph}</p>
            </div>
            <form action="/ajouter-offre-emploi" method="post" enctype="multipart/form-data" id="offreForm" novalidate>
                <#if error??>
                    <div style="background-color: #f8d7da; color: #721c24; padding: 15px; margin-bottom: 20px; border-radius: 5px; border: 1px solid #f5c6cb;">
                        <strong>Erreur !</strong> ${error}
                    </div>
                </#if>
                <#if data?? && data.errors?? && (data.errors?size > 0)>
                    <div style="background-color: #f8d7da; color: #721c24; padding: 15px; margin-bottom: 20px; border-radius: 5px; border: 1px solid #f5c6cb;">
                        <strong>Veuillez corriger les erreurs suivantes :</strong>
                        <ul style="margin: 10px 0 0 20px;">
                            <#list data.errors?keys as key>
                                <li>${data.errors[key]}</li>
                            </#list>
                        </ul>
                    </div>
                </#if>
                <div class="box-br">
                    <h2>Informations de base</h2>
                    <div class="form-group-two">
                        <div class="form-group" hidden="hidden">
                            <#if !data.job??>
                                <input name="key">
                            <#else>
                                <#if data.job.key??>
                                    <input name="key" value="${data.job.key}">
                                </#if>
                            </#if>
                            <#if secretCode??>
                                <input type="hidden" name="returnSecretCode" value="${secretCode}">
                            </#if>
                        </div>
                        <div class="form-group">
                            <label for="title">Titre *</label>
                            <#if data?? && data.job?? && data.job.title??>
                                <input type="text" name="title" id="title" value="${data.job.title}" required>
                            <#else>
                                <input type="text" name="title" id="title" required>
                            </#if>
                            <div class="field-error" id="title-error" aria-live="polite"></div>
                        </div>
                        <div class="form-group">
                            <label for="domain">Domain *</label>
                            <select name="domain" id="domain" required>
                                <option value="" disabled <#if !data?? || !data.job?? || !data.job.domain??>selected</#if>>Sélectionner un domaine</option>
                                <#if data?? && data.domains??>
                                    <#list data.domains as domain>
                                        <option value="${domain.slug}" 
                                            <#if data.job?? && data.job.domain?? && data.job.domain == domain.slug>selected</#if>>
                                            ${domain.name}
                                        </option>
                                    </#list>
                                </#if>
                            </select>
                            <div class="field-error" id="domain-error" aria-live="polite"></div>
                        </div>
                        <div class="form-group">
                            <label for="type">Type de contrat *</label>
                            <#if data?? && data.job?? && data.job.type??>
                                <input type="text" name="type" id="type" value="${data.job.type}" required>
                            <#else>
                                <input type="text" name="type" id="type" required>
                            </#if>
                            <div class="field-error" id="type-error" aria-live="polite"></div>
                        </div>
                        <div class="form-group">
                            <label for="city">Ville *</label>
                            <select name="city" id="city" required>
                                <option value="" disabled <#if !data?? || !data.job?? || !data.job.city??>selected</#if>>Sélectionner une ville</option>
                                <#if data?? && data.cities??>
                                    <#list data.cities as city>
                                        <option value="${city.slug}" 
                                            <#if data.job?? && data.job.city?? && data.job.city == city.slug>selected</#if>>
                                            ${city.name}
                                        </option>
                                    </#list>
                                </#if>
                            </select>
                            <div class="field-error" id="city-error" aria-live="polite"></div>
                        </div>
                        <div class="form-group">
                            <label for="nbrDePostes">Nombre de poste *</label>
                            <#if data?? && data.job?? && (data.job.nbrDePostes?? && data.job.nbrDePostes gt 0)>
                                <input type="text" name="nbrDePostes" id="nbrDePostes" value="${data.job.nbrDePostes}" required>
                            <#else>
                                <input type="text" name="nbrDePostes" id="nbrDePostes" required>
                            </#if>
                            <div class="field-error" id="nbrDePostes-error" aria-live="polite"></div>
                        </div>
                    </div>
                </div>
                <div class="box-br">
                    <h2>Informations de profil</h2>
                    <div class="form-group-two">
                        <div class="form-group">
                            <label for="formation">Formation *</label>
                            <select name="formation" id="formation" required>
                                <option value="" disabled <#if !data?? || !data.job?? || !data.job.formation??>selected</#if>>Sélectionner un niveau de formation</option>
                                <option value="Niveau moins de bac" <#if data?? && data.job?? && data.job.formation?? && data.job.formation == "Niveau moins de bac">selected</#if>>Niveau moins de bac</option>
                                <option value="bac" <#if data?? && data.job?? && data.job.formation?? && data.job.formation == "bac">selected</#if>>bac</option>
                                <option value="bac+1" <#if data?? && data.job?? && data.job.formation?? && data.job.formation == "bac+1">selected</#if>>bac+1</option>
                                <option value="bac+2" <#if data?? && data.job?? && data.job.formation?? && data.job.formation == "bac+2">selected</#if>>bac+2</option>
                                <option value="bac+3" <#if data?? && data.job?? && data.job.formation?? && data.job.formation == "bac+3">selected</#if>>bac+3</option>
                                <option value="bac+4" <#if data?? && data.job?? && data.job.formation?? && data.job.formation == "bac+4">selected</#if>>bac+4</option>
                                <option value="bac+5" <#if data?? && data.job?? && data.job.formation?? && data.job.formation == "bac+5">selected</#if>>bac+5</option>
                                <option value="bac+6" <#if data?? && data.job?? && data.job.formation?? && data.job.formation == "bac+6">selected</#if>>bac+6</option>
                                <option value="bac+7" <#if data?? && data.job?? && data.job.formation?? && data.job.formation == "bac+7">selected</#if>>bac+7</option>
                                <option value="Après bac+7" <#if data?? && data.job?? && data.job.formation?? && data.job.formation == "Après bac+7">selected</#if>>Après bac+7</option>
                            </select>
                            <div class="field-error" id="formation-error" aria-live="polite"></div>
                        </div>
                        <div class="form-group">
                            <label for="experienceLevel">Experience *</label>
                            <select name="experienceLevel" id="experienceLevel" required>
                                <option value="" disabled <#if !data?? || !data.job?? || !data.job.experienceLevel??>selected</#if>>Sélectionner un niveau d'expérience</option>
                                <option value="Moins de 1 an" <#if data?? && data.job?? && data.job.experienceLevel?? && data.job.experienceLevel == "Moins de 1 an">selected</#if>>Moins de 1 an</option>
                                <option value="De 1 à 3 ans" <#if data?? && data.job?? && data.job.experienceLevel?? && data.job.experienceLevel == "De 1 à 3 ans">selected</#if>>De 1 à 3 ans</option>
                                <option value="De 3 à 5 ans" <#if data?? && data.job?? && data.job.experienceLevel?? && data.job.experienceLevel == "De 3 à 5 ans">selected</#if>>De 3 à 5 ans</option>
                                <option value="De 5 à 10 ans" <#if data?? && data.job?? && data.job.experienceLevel?? && data.job.experienceLevel == "De 5 à 10 ans">selected</#if>>De 5 à 10 ans</option>
                                <option value="Plus de 10 ans" <#if data?? && data.job?? && data.job.experienceLevel?? && data.job.experienceLevel == "Plus de 10 ans">selected</#if>>Plus de 10 ans</option>
                            </select>
                            <div class="field-error" id="experienceLevel-error" aria-live="polite"></div>
                        </div>
                    </div>
                    <label for="content">Texte de l'annonce *</label>
                    <#if data?? && data.job?? && data.job.content??>
                        <textarea name="content" id="content" cols="30" rows="10" required>${data.job.content}</textarea>
                    <#else>
                        <textarea cols="30" rows="10" name="content" id="content" required></textarea>
                    </#if>
                    <div class="field-error" id="content-error" aria-live="polite"></div>
                </div>
                <div class="box-br">
                    <h2>Réseaux sociaux</h2>
                    <div class="form-group-two">
                        <div class="form-group">
                            <label for="facebook">Facebook *</label>
                            <#if data?? && data.job?? && data.job.facebook??>
                                <input type="text" name="facebook" id="facebook" value="${data.job.facebook}" required>
                            <#else>
                                <input type="text" name="facebook" id="facebook" required>
                            </#if>
                            <div class="field-error" id="facebook-error" aria-live="polite"></div>
                        </div>
                        <div class="form-group">
                            <label for="twitter">Twitter *</label>
                            <#if data?? && data.job?? && data.job.twitter??>
                                <input type="text" name="twitter" id="twitter" value="${data.job.twitter}" required>
                            <#else>
                                <input type="text" name="twitter" id="twitter" required>
                            </#if>
                            <div class="field-error" id="twitter-error" aria-live="polite"></div>
                        </div>
                        <div class="form-group">
                            <label for="linkedin">Linkedin *</label>
                            <#if data?? && data.job?? && data.job.linkedin??>
                                <input type="text" name="linkedin" id="linkedin" value="${data.job.linkedin}" required>
                            <#else>
                                <input type="text" name="linkedin" id="linkedin" required>
                            </#if>
                            <div class="field-error" id="linkedin-error" aria-live="polite"></div>
                        </div>
                    </div>
                </div>
                <div class="two-box-br">
                    <div class="box-br">
                        <h2>Entreprise</h2>
                        <div>
                            <label for="companyCode">Patente ou ICE *</label>
                            <#if data?? && data.job?? && data.job.companyCode??>
                                <input type="text" name="companyCode" id="companyCode" value="${data.job.companyCode}" required>
                            <#else>
                                <input type="text" name="companyCode" id="companyCode" required>
                            </#if>
                            <div class="field-error" id="companyCode-error" aria-live="polite"></div>
                        </div>
                        <div>
                            <label for="companyName">Nom de l'entreprise *</label>
                            <#if data?? && data.job?? && data.job.companyName?? >
                                <input type="text" name="companyName" id="companyName" value="${data.job.companyName}" required>
                            <#else>
                                <input type="text" name="companyName" id="companyName" required>
                            </#if>
                            <div class="field-error" id="companyName-error" aria-live="polite"></div>
                        </div>
                        <div>
                            <label for="email">Email *</label>
                            <#if data?? && data.job?? && data.job.email??>
                                <input type="email" name="email" id="email" value="${data.job.email}" required>
                            <#else>
                                <input type="email" name="email" id="email" required>
                            </#if>
                            <div class="field-error" id="email-error" aria-live="polite"></div>
                        </div>
                        <div>
                            <label for="tel">Téléphone *</label>
                            <#if data?? && data.job??  && data.job.phone??>
                                <input type="text" name="tel" id="tel" value="${data.job.phone}" required>
                            <#else>
                                <input type="text" name="tel" id="tel" required>
                            </#if>
                            <div class="field-error" id="tel-error" aria-live="polite"></div>
                        </div>
                        <div>
                            <label for="file"><img src="/assets/img/download.png" alt="">Télécharger Image </label>
                            <input type="file" name="logoImage" id="file">
                        </div>
                    </div>

                    <div class="box-br height-auto">
                        <h2>Confidentialité *</h2>
                        <div class="group-radio">
                            <input type="radio" name="confidentialite" id="id1" checked
                            value="Mes coordonnées sont publiques">
                            <label for="id1">Mes coordonnées sont publiques</label>
                        </div>
                        <div class="group-radio">
                            <input type="radio" name="confidentialite" id="id2"
                            value="Mes coordonnées sont transmises aux candidats">
                            <label for="id2">Mes coordonnées sont transmises aux candidats</label>
                        </div>
                        <div class="group-radio">
                            <input type="radio" name="confidentialite" id="id3"
                            value="Mes coordonnées sont privées (les candidats envoient leur cv depuis le site)">
                            <label for="id3">Mes coordonnées sont privées (les candidats envoient leur cv depuis le site)</label>
                        </div>

                        <div class="kaptcha-block" id="kaptcha-section">
                            <label>Vérification anti-robot *</label>
                            <img id="kaptchaImg" src="/captcha-image" alt="Code de vérification" title="Cliquer pour rafraîchir"
                                 onclick="this.src='/captcha-image?t='+new Date().getTime()">
                            <span class="kaptcha-hint">Cliquez sur l'image pour en obtenir une nouvelle</span>
                            <input type="text" name="kaptchaResponse" id="kaptchaResponse" placeholder="Saisir le code" required autocomplete="off">
                            <#if captchaError??>
                                <div class="field-error" id="captcha-error-msg">${captchaError}</div>
                            </#if>
                        </div>

                        <#if data?? && data.job?? && data.job.key??>
                            <button type="submit">Modifier</button>
                        <#else>
                            <button type="submit">Ajouter</button>
                        </#if>
                    </div>
                </div>
            </form>
        </section>

        <footer>
            <#include "../common/footer.ftl" />
        </footer>

        <script src="/assets/js-min/script.min.js" defer></script>
        <script src="/assets/js/offre.js" defer></script>
        <#if captchaError??>
        <script>
            document.addEventListener('DOMContentLoaded', function() {
                var el = document.getElementById('kaptcha-section');
                if (el) { el.scrollIntoView({ behavior: 'smooth', block: 'center' }); }
            });
        </script>
        </#if>
    </body>
</html>
