<!DOCTYPE html>
<html lang="en">
    <head class="head-section">
        <#include "/common/temp/head.ftl">

        <link rel="stylesheet" href="/assets/css-min/temp/style.min.css" />
        <link rel="stylesheet" href="/assets/css-min/temp/details-du-poste.min.css" />
    </head>
    <body>
        <header class="white-header" id="header">
            <#include "/common/temp/header.ftl">
        </header>

        <section class="breadcumb">
            <h1>Détail du poste</h1>
            <ul>
                <li><a href="/">Accueil</a></li>
                <li>${data.job.title}</li>
            </ul>
        </section>

        <section class="job-card">
            <div class="job-card-left">
                <#-- Logo de l'entreprise ou image par défaut -->
                <img
                    src="${data.job.img!'/assets/img/offer-1.png'}"
                    alt="${data.job.title}"
                    class="job-card__image"
                />
                <div class="job-card__info">
                    <h2 class="job-card__name">${data.job.title}</h2>
                    <div class="city">
                        <img src="/assets/img/place.png" alt="icon" />
                        <p class="job-card__details">${data.job.city!''}</p>
                        <#if data.job.type??>
                            <div class="contract">
                                <p class="job-card__contact">${data.job.type}</p>
                            </div>
                        </#if>
                    </div>

                    <div class="time-phone-email">
                        <div class="info-item">
                            <img src="/assets/img/calendar.png" alt="icon" />
                            <p>
                                <#-- On peut formater la date si disponible : ${data.job.postedDate?string("dd MMM yyyy")} -->
                                <#-- ${data.job.postedAgo!''} -->
                                Posté le ${data.job.creationDate?string("dd/MM/yyyy")}

                            </p>
                        </div>

                        <#if data.job.phone??>
                            <div class="info-item">
                                <img src="/assets/img/phone.png" alt="icon" />
                                <p>${data.job.phone}</p>
                            </div>
                        </#if>
                        <#if data.job.email??>
                            <div class="info-item">
                                <img src="/assets/img/email.png" alt="icon" />
                                <p>${data.job.email}</p>
                            </div>
                        </#if>
                    </div>
                </div>
            </div>
            <div class="job-card__right">
                <button
                class="job-card__apply-btn"
                onclick="window.location.href='/postuler-emploi/${data.job.key}'"
                >
                    Postuler
                </button>
            </div>
        </section>

        <section class="job-section">
            <div class="job-left">
                <h2>Description du poste</h2>
                <p>
                    ${data.job.content?html}
                </p>
            </div>
            <div class="job-right">
                <h3>Informations complémentaires</h3>
                <ul class="job-details">
                    <li>
                        <img src="/assets/img/work.png" alt="icon" />
                        <div class="detail-text">
                            <strong>Domaine :</strong>
                            <span class="subtext">${data.job.domain!''}</span>
                        </div>
                    </li>
                    <li>
                        <img src="/assets/img/school.png" alt="icon" />
                        <div class="detail-text">
                            <strong>Formation :</strong>
                            <span class="subtext">${data.job.formation!''}</span>
                        </div>
                    </li>
                    <li>
                        <img src="/assets/img/experience.png" alt="icon" />
                        <div class="detail-text">
                            <strong>Expérience :</strong>
                            <span class="subtext">${data.job.experienceLevel!''}</span>
                        </div>
                    </li>
                    <li>
                        <img src="/assets/img/person_outline.png" alt="icon" />
                        <div class="detail-text">
                            <strong>Nombre de postes :</strong>
                            <span class="subtext">${data.job.nbrDePostes!1} poste(s)</span>
                        </div>
                    </li>
                </ul>
            </div>
        </section>

     <section class="table-offre">
            <#list data.similarOffre as job>
                <div class="offre">

                    <img
                        src="${job.img!'/assets/img/offer-1.png'}"
                        alt="${job.title}"
                        onerror="this.onerror=null; this.src='/assets/img/offer-1.png';"
                    />

                    <div>
                        <h3><a href="/offre-emploi-maroc/${job.key}" title="${job.title}">${job.title}</a></h3>
                        <span><img src="/assets/img/place.png" alt="Lieu" />${job.city!''}</span>
                        <span><img src="/assets/img/business_center.png" alt="Secteur" />${job.domain!job.type!''}</span>
                        <span class="orange">(${job.nbrDePostes!1} poste(s))</span>
                    </div>
                <div>
                <#if job.type?? && job.type?has_content>
                    <span class="badge">${job.type}</span>
                </#if>
                <#--<b>${job.postedAgo!''}</b>-->
                <b>Posté le ${data.job.creationDate?string("dd/MM/yyyy")}</b>

                </div>
                </div>
            </#list>
    </section>

        <#include "/common/footer.ftl">

        <script src="/assets/js-min/script.min.js" defer></script>
    </body>
</html>