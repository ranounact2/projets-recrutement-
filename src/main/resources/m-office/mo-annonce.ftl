<!DOCTYPE html>
<html lang = "en">
<head>
    <#include "../common/head.ftl" />
    <link rel="stylesheet" href="/assets/css-min/style.min.css" defer/>
    <link rel="stylesheet" href="/assets/css-min/annonce.min.css" defer/>
</head>
<body>
<header>
    <#include "/common/header.ftl">
</header>


<!-- bread -->
<div class="bread">
    <div class="titre">
        <h2>Mes offres d'emploi</h2>
        <div class="breadcrumbs" id="breadcrumbs">
            <a class="breadcrumbs__link" href="/">Accueil</a>
            <span> > </span>
            <br>
            <a class="breadcrumbs__link" href="/a-propos-emplois-maroc">Apropos</a>
            <span> > </span>
            <br>
            <span>Mes offres d'emplois</span>
        </div>
    </div>
</div>

<!-- offres -->
<section class="offres">
    <#if deleted?? && deleted == "true">
        <div style="background-color: #d4edda; color: #155724; padding: 15px; margin: 20px; border-radius: 5px; border: 1px solid #c3e6cb;">
            <strong>Succès !</strong> L'offre a été supprimée avec succès.
        </div>
    </#if>
    <#if updated?? && updated == "true">
        <div style="background-color: #d4edda; color: #155724; padding: 15px; margin: 20px; border-radius: 5px; border: 1px solid #c3e6cb;">
            <strong>Succès !</strong> L'offre a été modifiée avec succès.
        </div>
    </#if>
    <#if ads?size == 0>
        <div class="table-offre">
            <div style="text-align: center; padding: 40px; min-height: 300px; display: flex; align-items: center; justify-content: center; flex-direction: column;">
                <h2 style="color: #333; margin-bottom: 20px;">Aucune offre trouvée</h2>
                <p style="color: #666; font-size: 16px; margin-bottom: 30px;">
                    Désolé, nous n'avons trouvé aucune offre d'emploi associée à ce lien.
                </p>
                <p style="color: #666; font-size: 14px;">
                    <a href="/mes-annonces-emploi" style="color: #1f62b6; text-decoration: none;">Demander un nouveau lien</a> ou <a href="/" style="color: #1f62b6; text-decoration: none;">retour à la page d'accueil</a>
                </p>
            </div>
        </div>
    <#else>
        <div class="slide">
            <#list ads as job>
                <div class="item">
                    <img src="/assets/img/save.png" class="save" alt="" srcset="">
                    <div class="heading">
                        <#if job.img??>
                            <img src="/assets/img/${job.img}" alt="company logo">
                        <#else>
                            <img src="/assets/img/company-1.png" alt="company logo">
                        </#if>
                        <div class="div2">
                            <h2>${job.title}</h2>
                            <p class="open-icon">
                                <i class="far fa-clock"></i>
                                <span class="creation-date">${job.creationDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                            </p>
                            <img src="/assets/img/business_center.png" alt="domain">
                            <span>${job.domain}</span>
                            <img src="/assets/img/place.png" alt="city">
                            <span>${job.city}</span>
                        </div>
                    </div>
                    <#if job.type == 'stage'>
                        <span class="badge danger">${job.type}</span>
                    <#else>
                        <span class="badge success">${job.type}</span>
                    </#if>
                    <div class="cl1">
                            <span class="orange">
                                <#if job.announcetype == 0>Scrapy
                                <#elseif job.announcetype == 1>Scrapy with email
                                <#elseif job.announcetype == 2>Star
                                <#else>Normal
                                </#if>
                            </span>
                        <span class="orange">
                                <#if job.nbrDePostes??>Nombre de poste: ${job.nbrDePostes}</#if>
                            </span>

                        <div>
                            <b>Détails</b><br/>
                            <p>${job.content}</p>
                        </div>
                        <div class="btn-group">
                            <a class="btn" href="/m-office/mes-annonces/update/${job.secretCode}/${job.key}">Modifier</a>
                            <a class="btn btn-danger" href="/m-office/mes-annonces/delete/${job.secretCode}/${job.key}" 
                               onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette offre ? Cette action est irréversible.');">Supprimer</a>
                        </div>
                    </div>

                </div>
            </#list>
        </div>
        <#if totalPages gt 1>
            <div class="pagination">
                <#if pageNumber gt 1>
                    <a href="?page=${pageNumber - 1}&size=${adsPerPage}" class="pagination-link">Previous</a>
                    <span class="pagination-link"><a href="?page=${pageNumber - 1}&size=${adsPerPage}" class="pagination-link">${pageNumber - 1}</a></span>
                </#if>
                <span class="pagination-link selected current-page"><a href="?page=${pageNumber}&size=${adsPerPage}" class="pagination-link">${pageNumber}</a></span>
                <#if pageNumber lt totalPages>
                    <span class="pagination-link"><a href="?page=${pageNumber + 1}&size=${adsPerPage}" class="pagination-link">${pageNumber + 1}</a></span>
                    <a href="?page=${pageNumber + 1}&size=${adsPerPage}" class="pagination-link">Next</a>
                </#if>
            </div>
        </#if>
    </#if>
</section>

<footer>
    <#include "/common/footer.ftl">
</footer>

<script src="/assets/js-min/script.min.js" defer></script>

</body>
</html>
