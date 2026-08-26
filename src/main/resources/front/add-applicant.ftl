<!DOCTYPE html>
<html lang = "en">

<head>
    <#include "../common/head.ftl">
    <link rel="stylesheet" href="/assets/css-min/temp/style.min.css" />
    <link rel="stylesheet" href="/assets/css-min/add-applicant.min.css" defer/>
</head>

<body>

<header>
    <#include "../common/header.ftl">
</header>

<#-- Récupération des valeurs depuis les zones -->
<#assign zoneH1 = "Ajouter une candidature">
<#assign zoneParagraph = "Prenez quelques minutes pour réunir toutes les informations utiles avant de remplir ce formulaire. Rédigez une motivation concise, évitez les répétitions et mettez en avant vos compétences clés liées à l'offre sélectionnée.">

<#if zones??>
    <#list zones as zonePage>
        <#if zonePage.zones??>
            <#list zonePage.zones?keys as zoneKey>
                <#assign currentZone = zonePage.zones[zoneKey]>
                <#if currentZone.values??>
                    <#if currentZone.values.h1??>
                        <#assign zoneH1 = currentZone.values.h1.content!currentZone.values.h1.value!''>
                    </#if>
                    <#if currentZone.values.h1_block??>
                        <#assign zoneParagraph = currentZone.values.h1_block.content!currentZone.values.h1_block.value!''>
                    </#if>
                </#if>
            </#list>
        </#if>
    </#list>
</#if>

<section class="breadcumb">
    <h1>${zoneH1}</h1>
    <ul>
        <li><a href="/">Accueil</a></li>
        <li><a href="http://">Ajouter une candidature</a></li>
    </ul>
</section>
<section class="applicant">
    <p class="box-parag">
        ${zoneParagraph}
    </p>

    <#if data.job??>
        <div class="selected-job-card">
            <div>
                <p class="selected-job-label">Offre sélectionnée</p>
                <h2>${data.job.title}</h2>
                <p class="selected-job-meta">
                    ${data.job.city!""}
                    <#if data.job.domain?? && data.job.domain?has_content>
                        · ${data.job.domain}
                    </#if>
                </p>
            </div>
            <button type="button" class="btn-white" onclick="window.location.href='/offre-emploi-maroc/${data.job.key}'">
                Voir l'offre
            </button>
        </div>
    </#if>

    <#if (data.errors?? && (data.errors?size > 0))>
        <div class="error-messages" style="color: #dc3545; background: #f8d7da; padding: 1rem; border-radius: 8px; margin-bottom: 1.5rem; border: 1px solid #f5c6cb;">
            <strong>Veuillez corriger les erreurs suivantes :</strong>
            <ul style="margin: 0.5rem 0 0 1.5rem; padding: 0;">
                <#list data.errors as field, error>
                    <li>${error}</li>
                </#list>
            </ul>
        </div>
    </#if>

    <form action="/postuler-emploi/${data.job.key}" method="post" enctype="multipart/form-data">
        <div class="form-group-two">
            <div class="form-group">
                <label for="nom">Nom <span style="color: red;">*</span></label>
                <input type="text" name="nom" id="nom" placeholder="Dupont" 
                       value="${data.nom!''}" 
                       class="<#if data.errors?? && data.errors.nom??>error-field</#if>">
                <#if data.errors?? && data.errors.nom??>
                    <span class="field-error" style="color: #dc3545; font-size: 0.875rem; display: block; margin-top: 0.25rem;">${data.errors.nom}</span>
                </#if>
            </div>
            <div class="form-group">
                <label for="prenom">Prénom <span style="color: red;">*</span></label>
                <input type="text" name="prenom" id="prenom" placeholder="Camille" 
                       value="${data.prenom!''}" 
                       class="<#if data.errors?? && data.errors.prenom??>error-field</#if>">
                <#if data.errors?? && data.errors.prenom??>
                    <span class="field-error" style="color: #dc3545; font-size: 0.875rem; display: block; margin-top: 0.25rem;">${data.errors.prenom}</span>
                </#if>
            </div>
            <div class="form-group">
                <label for="email">Email <span style="color: red;">*</span></label>
                <input type="email" name="email" id="email" placeholder="camille.dupont@email.com" 
                       value="${data.email!''}" 
                       class="<#if data.errors?? && data.errors.email??>error-field</#if>">
                <#if data.errors?? && data.errors.email??>
                    <span class="field-error" style="color: #dc3545; font-size: 0.875rem; display: block; margin-top: 0.25rem;">${data.errors.email}</span>
                </#if>
            </div>
            <div class="form-group">
                <label for="phone">Téléphone <span style="color: red;">*</span></label>
                <input type="tel" name="phone" id="phone" placeholder="06 12 34 56 78" 
                       value="${data.phone!''}" 
                       class="<#if data.errors?? && data.errors.phone??>error-field</#if>">
                <#if data.errors?? && data.errors.phone??>
                    <span class="field-error" style="color: #dc3545; font-size: 0.875rem; display: block; margin-top: 0.25rem;">${data.errors.phone}</span>
                </#if>
            </div>
        </div>
        <label for="motivation">Motivation <span style="color: red;">*</span></label>
        <textarea name="motivation" id="motivation" cols="30" rows="10" 
                  placeholder="Expliquez en quelques lignes votre intérêt pour ce poste..."
                  class="<#if data.errors?? && data.errors.motivation??>error-field</#if>">${data.motivation!''}</textarea>
        <#if data.errors?? && data.errors.motivation??>
            <span class="field-error" style="color: #dc3545; font-size: 0.875rem; display: block; margin-top: 0.25rem;">${data.errors.motivation}</span>
        </#if>
        <div class="form-group-two">
            <div class="form-group">
                <label for="formation">Formation <span style="color: red;">*</span></label>
                <select name="formation" id="formation" 
                       class="<#if data.errors?? && (data.errors.formation?? || data.errors.experience??)>error-field</#if>">
                    <option value="" disabled <#if !data?? || !data.formation??>selected</#if>>Sélectionner un niveau de formation</option>
                    <option value="Niveau moins de bac" <#if data?? && data.formation?? && data.formation == "Niveau moins de bac">selected</#if>>Niveau moins de bac</option>
                    <option value="bac" <#if data?? && data.formation?? && data.formation == "bac">selected</#if>>bac</option>
                    <option value="bac+1" <#if data?? && data.formation?? && data.formation == "bac+1">selected</#if>>bac+1</option>
                    <option value="bac+2" <#if data?? && data.formation?? && data.formation == "bac+2">selected</#if>>bac+2</option>
                    <option value="bac+3" <#if data?? && data.formation?? && data.formation == "bac+3">selected</#if>>bac+3</option>
                    <option value="bac+4" <#if data?? && data.formation?? && data.formation == "bac+4">selected</#if>>bac+4</option>
                    <option value="bac+5" <#if data?? && data.formation?? && data.formation == "bac+5">selected</#if>>bac+5</option>
                    <option value="bac+6" <#if data?? && data.formation?? && data.formation == "bac+6">selected</#if>>bac+6</option>
                    <option value="bac+7" <#if data?? && data.formation?? && data.formation == "bac+7">selected</#if>>bac+7</option>
                    <option value="Après bac+7" <#if data?? && data.formation?? && data.formation == "Après bac+7">selected</#if>>Après bac+7</option>
                </select>
                <#if data.errors?? && data.errors.formation??>
                    <span class="field-error" style="color: #dc3545; font-size: 0.875rem; display: block; margin-top: 0.25rem;">${data.errors.formation}</span>
                </#if>
            </div>
            <div class="form-group">
                <label for="experienceLevel">Années d'expériences <span style="color: red;">*</span></label>
                <select name="experienceLevel" id="experienceLevel" 
                       class="<#if data.errors?? && (data.errors.experience?? || data.errors.experienceLevel??)>error-field</#if>">
                    <option value="" disabled <#if !data?? || !data.experienceLevel??>selected</#if>>Sélectionner un niveau d'expérience</option>
                    <option value="Moins de 1 an" <#if data?? && data.experienceLevel?? && data.experienceLevel == "Moins de 1 an">selected</#if>>Moins de 1 an</option>
                    <option value="De 1 à 3 ans" <#if data?? && data.experienceLevel?? && data.experienceLevel == "De 1 à 3 ans">selected</#if>>De 1 à 3 ans</option>
                    <option value="De 3 à 5 ans" <#if data?? && data.experienceLevel?? && data.experienceLevel == "De 3 à 5 ans">selected</#if>>De 3 à 5 ans</option>
                    <option value="De 5 à 10 ans" <#if data?? && data.experienceLevel?? && data.experienceLevel == "De 5 à 10 ans">selected</#if>>De 5 à 10 ans</option>
                    <option value="Plus de 10 ans" <#if data?? && data.experienceLevel?? && data.experienceLevel == "Plus de 10 ans">selected</#if>>Plus de 10 ans</option>
                </select>
                <#if data.errors?? && data.errors.experience??>
                    <span class="field-error" style="color: #dc3545; font-size: 0.875rem; display: block; margin-top: 0.25rem;">${data.errors.experience}</span>
                </#if>
            </div>
        </div>
        <div class="upload">
            <b>Ajouter votre CV <span style="color: red;">*</span></b>
            <div class="alert-purpel">
                Remarque ! Seuls les fichiers pdf sont acceptés. Taille maximale 5 Mo.
            </div>
            <label for="cv">Upload CV</label>
            <input type="file" name="uploaded_file" id="cv" accept=".pdf,application/pdf"
                   class="<#if data.errors?? && data.errors.cv??>error-field</#if>">
            <#if data.errors?? && data.errors.cv??>
                <span class="field-error" style="color: #dc3545; font-size: 0.875rem; display: block; margin-top: 0.25rem;">${data.errors.cv}</span>
            </#if>
        </div>
        <div class="form-actions">
            <button type="submit" class="btn-orange">Postuler</button>
        </div>
    </form>
</section>

<footer>
    <#include "../common/footer.ftl">
</footer>

<script src="/assets/js/add-applicant-validation.js" defer></script>
<script src="/assets/js-min/script.min.js" defer></script>
</body>