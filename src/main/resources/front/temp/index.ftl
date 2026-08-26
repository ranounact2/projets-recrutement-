<!DOCTYPE html>
<html lang="fr">
    <#include "/common/temp/head.ftl">
    <body class="home-page">
        <link rel="stylesheet" href="/assets/css/index.css" defer/>
        <#include "/common/temp/header.ftl">

        <#-- ============================ HERO ============================ -->
        <section class="intro jc-hero">
            <div class="jc-wrap jc-hero-inner">
                <#assign h1Content = "Des offres d'emploi au Maroc.">
                <#if zones??>
                    <#list zones as zonePage>
                        <#if zonePage.zones??>
                            <#list zonePage.zones?keys as zoneKey>
                                <#assign currentZone = zonePage.zones[zoneKey]>
                                <#if currentZone.values?? && currentZone.values.h1??>
                                    <#assign h1Content = currentZone.values.h1.content!currentZone.values.h1.value!''>
                                    <#break>
                                </#if>
                            </#list>
                        </#if>
                    </#list>
                </#if>
                <h1>${h1Content}</h1>

                <#assign paragraphContent = "Votre nouvel emploi vous attend">
                <#if zones??>
                    <#list zones as zonePage>
                        <#if zonePage.zones??>
                            <#list zonePage.zones?keys as zoneKey>
                                <#assign currentZone = zonePage.zones[zoneKey]>
                                <#if currentZone.values?? && currentZone.values.h1_block??>
                                    <#assign paragraphContent = currentZone.values.h1_block.content!currentZone.values.h1_block.value!''>
                                    <#break>
                                </#if>
                            </#list>
                        </#if>
                    </#list>
                </#if>
                <p class="jc-hero-sub">${paragraphContent}</p>

                <form action="" class="jc-searchbox">
                    <div class="jc-searchgroup">
                        <i class="fas fa-magnifying-glass"></i>
                        <input class="inpt" id="keyword" type="text" placeholder="Titre, mots clés ou entreprise">
                    </div>
                    <div class="jc-searchgroup">
                        <i class="fas fa-location-dot"></i>
                        <select name="" id="city">
                            <option value="" selected>Toutes les villes</option>
                            <#list data.cities as city>
                                <option value="${city.slug}">${city.name}</option>
                            </#list>
                        </select>
                    </div>
                    <div class="jc-searchgroup">
                        <i class="fas fa-briefcase"></i>
                        <select name="" id="domain">
                            <option value="" selected>Tous les domaines</option>
                            <#list data.domains as domain>
                                <option value="${domain.slug}">${domain.name}</option>
                            </#list>
                        </select>
                    </div>
                    <button id="linkButton" type="button" class="jc-btn jc-btn-primary jc-searchbtn">
                        Rechercher
                    </button>
                </form>

                <div class="jc-popular">
                    <span>Populaire :</span>
                    <#list data.domains as domain>
                        <#if domain?counter <= 5>
                            <a href="/categorie/${domain.slug}">${domain.name}</a>
                        </#if>
                    </#list>
                </div>

            </div>
        </section>

        <#-- ==================== SÉLECTION DU MOIS ==================== -->
        <section class="jc-featured">
            <div class="jc-wrap">
                <h2>Les offres d'emploi du mois</h2>
                <p class="jc-lead">Une sélection d'annonces à ne pas manquer ce mois-ci.</p>

                <div class="slide jc-featured-grid">
                    <#list data.jobs as job>
                        <#if job?counter <= 6>
                            <article class="item jc-featcard">
                                <span class="jc-featbadge">${job.type!'Offre'}</span>

                                <div class="jc-feathead">
                                    <#if job.img??>
                                        <img src="${job.img}" alt="${job.title?default('Offre')}" />
                                    <#else>
                                        <img src="/assets/img/offer-1.png" alt="Offre" />
                                    </#if>
                                    <div>
                                        <#if job.title??>
                                            <a href="/offre-emploi-maroc/${job.key}" title="${job.title}">
                                                <h3>${job.title}</h3>
                                            </a>
                                        <#else>
                                            <h3>Offre d'emploi</h3>
                                        </#if>
                                        <p class="jc-jobcompany">${job.domain!job.type!''}</p>
                                    </div>
                                </div>

                                <div class="jc-jobmeta">
                                    <span><i class="fas fa-location-dot"></i>${job.city!'N/C'}</span>
                                    <span class="jc-postes">${job.positionsCount!1} poste(s)</span>
                                </div>

                                <#if job.title??>
                                    <a class="jc-btn jc-btn-primary jc-btn-block jc-btn-sm" href="/offre-emploi-maroc/${job.key}">Voir l'offre</a>
                                </#if>
                            </article>
                        </#if>
                    </#list>
                </div>
            </div>
        </section>

        <#-- ==================== VILLES QUI RECRUTENT ==================== -->
        <section class="city jc-cities">
            <div class="jc-wrap">
                <#assign h2VilleContent = "Les villes qui recrutent">
                <#if zones??>
                    <#list zones as zonePage>
                        <#if zonePage.zones??>
                            <#list zonePage.zones?keys as zoneKey>
                                <#assign currentZone = zonePage.zones[zoneKey]>
                                <#if currentZone.values?? && currentZone.values.h2_block_ville??>
                                    <#assign h2VilleContent = currentZone.values.h2_block_ville.content!currentZone.values.h2_block_ville.value!''>
                                    <#break>
                                </#if>
                            </#list>
                        </#if>
                    </#list>
                </#if>
                <h2>${h2VilleContent}</h2>

                <#assign textVilleContent = "Découvrez les offres d'emploi dans les villes avec le plus grand nombre d'opportunités d'emploi.">
                <#if zones??>
                    <#list zones as zonePage>
                        <#if zonePage.zones??>
                            <#list zonePage.zones?keys as zoneKey>
                                <#assign currentZone = zonePage.zones[zoneKey]>
                                <#if currentZone.values?? && currentZone.values.text_block_ville??>
                                    <#assign textVilleContent = currentZone.values.text_block_ville.content!currentZone.values.text_block_ville.value!''>
                                    <#break>
                                </#if>
                            </#list>
                        </#if>
                    </#list>
                </#if>
                <p class="jc-lead">${textVilleContent}</p>

                <div class="jc-citygrid">
                    <a class="jc-citycard" href="/region/casablanca-settat/casablanca">
                        <img src="/assets/img/casablanca.png" alt="Casablanca" />
                        <h3>Casablanca</h3>
                        <span class="jc-btn jc-btn-outline jc-btn-sm">Voir les offres</span>
                    </a>
                    <a class="jc-citycard" href="/region/tanger-tetouan-al-hoceima/tanger">
                        <img src="/assets/img/tanger.png" alt="Tanger" />
                        <h3>Tanger</h3>
                        <span class="jc-btn jc-btn-outline jc-btn-sm">Voir les offres</span>
                    </a>
                    <a class="jc-citycard" href="/region/fes-meknes/fes">
                        <img src="/assets/img/fes.png" alt="Fès" />
                        <h3>Fès</h3>
                        <span class="jc-btn jc-btn-outline jc-btn-sm">Voir les offres</span>
                    </a>
                    <a class="jc-citycard" href="/region/fes-meknes/meknes">
                        <img src="/assets/img/meknes.png" alt="Meknès" />
                        <h3>Meknès</h3>
                        <span class="jc-btn jc-btn-outline jc-btn-sm">Voir les offres</span>
                    </a>
                    <a class="jc-citycard" href="/region/rabat-sale-kenitra/rabat">
                        <img src="/assets/img/rabat.png" alt="Rabat" />
                        <h3>Rabat</h3>
                        <span class="jc-btn jc-btn-outline jc-btn-sm">Voir les offres</span>
                    </a>
                    <a class="jc-citycard" href="/region/souss-massa/agadir">
                        <img src="/assets/img/Agadir.jpg" alt="Agadir" />
                        <h3>Agadir</h3>
                        <span class="jc-btn jc-btn-outline jc-btn-sm">Voir les offres</span>
                    </a>
                    <a class="jc-citycard" href="/region/dakhla-oued-ed-dahab/dakhla">
                        <img src="/assets/img/dakhla.jpg" alt="Dakhla" />
                        <h3>Dakhla</h3>
                        <span class="jc-btn jc-btn-outline jc-btn-sm">Voir les offres</span>
                    </a>
                    <a class="jc-citycard" href="/region/laayoune-sakia-el-hamra/laayoune">
                        <img src="/assets/img/laayoune.jpg" alt="Laâyoune" />
                        <h3>Laâyoune</h3>
                        <span class="jc-btn jc-btn-outline jc-btn-sm">Voir les offres</span>
                    </a>
                </div>

                <div class="jc-more">
                    <a class="jc-btn jc-btn-outline" href="/region">Voir toutes les villes</a>
                </div>
            </div>
        </section>

        <#-- ==================== DOMAINES ==================== -->
        <section class="domaine jc-domains">
            <div class="jc-wrap">
                <#assign h2CategorieContent = "Domaines populaires">
                <#if zones??>
                    <#list zones as zonePage>
                        <#if zonePage.zones??>
                            <#list zonePage.zones?keys as zoneKey>
                                <#assign currentZone = zonePage.zones[zoneKey]>
                                <#if currentZone.values?? && currentZone.values.h2_block_categorie??>
                                    <#assign h2CategorieContent = currentZone.values.h2_block_categorie.content!currentZone.values.h2_block_categorie.value!''>
                                    <#break>
                                </#if>
                            </#list>
                        </#if>
                    </#list>
                </#if>
                <h2>${h2CategorieContent}</h2>

                <#assign textBlockCategorie = "">
                <#if zones??>
                    <#list zones as zonePage>
                        <#if zonePage.zones??>
                            <#list zonePage.zones?keys as zoneKey>
                                <#assign currentZone = zonePage.zones[zoneKey]>
                                <#if currentZone.values?? && currentZone.values.text_block_categorie??>
                                    <#assign textBlockCategorie = currentZone.values.text_block_categorie.content!currentZone.values.text_block_categorie.value!''>
                                    <#break>
                                </#if>
                            </#list>
                        </#if>
                    </#list>
                </#if>
                <#if textBlockCategorie?has_content>
                    <p class="jc-lead">${textBlockCategorie}</p>
                </#if>

                <div class="jc-domaingrid">
                    <#list data.domains as domain>
                        <#if domain?counter <= 8>
                            <div class="domain-item">
                                <a class="jc-domaincard" href="/categorie/${domain.slug}">
                                    <span class="jc-domainicon"><i id="large" class="${domain.icon!'fas fa-briefcase'}"></i></span>
                                    <h4>${domain.name}</h4>
                                    <span class="jc-domainlink">Voir les offres</span>
                                </a>
                            </div>
                        </#if>
                    </#list>

                    <#if (data.domains?size > 8)>
                        <#list data.domains as domain>
                            <#if (domain?counter > 8)>
                                <div class="domain-item domain-hidden" style="display: none;">
                                    <a class="jc-domaincard" href="/categorie/${domain.slug}">
                                        <span class="jc-domainicon"><i id="large" class="${domain.icon!'fas fa-briefcase'}"></i></span>
                                        <h4>${domain.name}</h4>
                                        <span class="jc-domainlink">Voir les offres</span>
                                    </a>
                                </div>
                            </#if>
                        </#list>
                    </#if>
                </div>

                <div class="jc-more">
                    <#if (data.domains?size > 8)>
                        <a href="#" id="voir-plus-btn" class="jc-btn jc-btn-outline" onclick="toggleDomains(event)">Voir plus</a>
                        <a href="#" id="voir-moins-btn" class="jc-btn jc-btn-outline" onclick="toggleDomains(event)" style="display: none;">Voir moins</a>
                    <#else>
                        <a href="/categorie" class="jc-btn jc-btn-outline">Voir plus</a>
                    </#if>
                </div>
            </div>
        </section>

        <#-- ==================== RÉGIONS ==================== -->
        <section class="regions jc-regions">
            <div class="jc-wrap">
                <h2>Explorer par région</h2>
                <p class="jc-lead">Cliquez sur votre région pour voir les offres qui s'y trouvent.</p>

                <div class="jc-mapwrap">
                    <svg class="jc-map" viewBox="0 0 1000 1000" role="img" aria-label="Carte des régions du Maroc">
                        <a href="/region/tanger-tetouan-al-hoceima" class="jc-region" data-r="MA01">
                            <title>Tanger-Tétouan-Al Hoceima</title>
                            <path d="M774.8 92.2l1.1 1.6l-1.4 19.6l-10.8 2.7l-5.7 5.3l-3.6 -1.5l-1.9 -2.5l-4 5.7l-4.9 0.4l-0.9 2.7l-3.8 -2.1l-3.5 1.2l-2.1 -2.5l-5.8 0.2l-2.9 -2l-0.6 -5.9l-5 0.6l-4.9 2.7l-2.3 -0.7l-6.5 2.3l-1.9 1.7l-0.8 11.5l-3.2 0.2l-1.2 2.6l-5.7 -1.4l-0.3 1l-1.9 -1.2l0.5 -4.4l-5.6 -1.9l-3.3 -5.3l-2.8 -1.2l-3.7 2.3l-0.7 -1.7l-3.5 -1.3l0.7 -11.3l-7.3 -0.8l-4.6 -3.3l-2.4 1l-2.5 -2.5l-4.3 1.7l-3.2 -0.8l11.5 -31l4.9 -19.7l4.4 -0.9l3.5 1.6l3.2 -3.5l6.9 -0.1l5.6 -5l4.3 -0.8l3 3.3l0.3 9.9l1 2.3l2.4 -0.3l1.7 8.9l3.5 1.1l5.8 8l2.5 0.3l4.8 5.6l7.5 5.1l21.5 5.8l23.3 -7.4l1.2 3.2l4.4 0.9z"/>
                        </a>
                        <a href="/region/l-oriental" class="jc-region" data-r="MA02">
                            <title>L'Oriental</title>
                            <path d="M804.8 311.8l7.3 -7.8l-4.2 1.7l-4 -9.8l0.4 -7.5l3.6 -5.3l-7.4 4.4l-9.3 -2.5l-5.5 2.3l-1.6 -1.7l3.3 -3.8l-9 -0.2l0 -3.7l-3.1 0.3l-0.5 -5.9l-5.8 -8l0.7 -3.4l-5.2 -4.6l7.9 -13.5l13.9 -16.9l1.9 -0.9l7 3.2l4.9 -0.2l16.5 -3.4l6.3 -3.9l0.1 -2.5l-3.9 -5.3l-14.6 -13.6l11.9 -13.6l-0.7 -2.1l-13.5 -0.8l-11.3 -8.3l-7 10.7l-8 5.3l-2.9 4l3.3 2.2l-2.6 3.1l-1.6 -3.9l-3.8 3.5l-5.9 -0.7l-2.7 -2.2l1.3 -4.9l-2 -1.6l8.6 -4.4l-0.8 -3.1l3.3 -4.8l1 -7l3.6 -2.4l-1.5 -1.4l2 -11.5l-4.2 -0.9l0.6 -1.9l-1.3 -0.4l-0.4 -2.6l2.8 -0.3l1.5 -2l-1.3 -2.1l1.3 -2.7l-1.1 0.1l0.2 -2.2l2.9 -1.9l2.7 -4.5l-1.5 -4.7l2.6 -1.3l4.4 0.5l0.3 -2.6l2.3 -1.8l-1.5 -2.5l1.4 -3.1l-0.6 -3.5l-7.8 -0l-0.9 -2.8l-2.6 0l0.9 -18.4l-1.1 -1.6l2.6 -1.3l1.3 -3.6l2.3 -0.9l6.4 4.2l13 2.5l7.7 -2.8l2.9 -3.6l2.6 0.3l5.7 -10.3l1.4 0.8l-0.8 8.9l1.2 1.3l1.3 -1l-0.3 1.3l3.4 3.4l-3.8 -2.5l1.7 2.4l-1.4 1.8l2.2 3.4l3.7 1.9l3.6 -0.7l-5.8 -6.3l4.8 5l6 2.8l6.1 0.3l5.6 -4l6.9 1.9l4.4 2.1l-0.4 2.4l9.1 7.8l3.8 -0.6l0.1 3.4l4.1 2.6l0.3 2.3l7.8 4l-5.6 7.8l4.4 6.4l4 2l-5 6.1l4 5.6l-0.1 4.8l3.3 7.9l-2.8 15l1.6 6.1l-3.2 2.5l-0.5 2.1l2.2 1.6l2.3 -0.3l3 4.7l0.4 4.8l-3.9 9.6l-0.4 6l5.1 8.5l5.2 4.5l-0.3 5.4l-3.3 1.9l8.4 13.5l5.9 2.9l14.3 11.4l-6.5 6.1l-3.6 0.6l-3 4.7l0.3 7.8l-3.1 2.4l5.9 -0.1l1.9 3.5l-2.7 1.7l-19.8 -0.5l-17.4 -4.5l-3.3 0.4l-2.2 -1.5l-7.1 2.4l-15.6 -1l-5.2 0.9l-2.8 2l-14.4 0.1l-2.5 2.5l-0.4 2.6l4.5 8.8l1.2 5.8l-22.7 4.9z"/>
                        </a>
                        <a href="/region/fes-meknes" class="jc-region" data-r="MA03">
                            <title>Fès-Meknès</title>
                            <path d="M696.4 135.9l1.8 0.1l1.2 -2.6l3.2 -0.2l0.8 -11.5l1.5 -1.4l6.9 -2.6l2.3 0.7l4.9 -2.7l5 -0.6l0.6 5.9l2.9 2l5.8 -0.2l2.1 2.5l3.5 -1.2l3.8 2.1l0.9 -2.7l4.9 -0.4l4 -5.7l1.9 2.5l3.6 1.5l5.7 -5.3l9.7 -2.3l1.6 -1.6l2.6 -0l0.9 2.8l7.8 0l0.6 3.5l-1.4 3.1l1.5 2.5l-2.3 1.8l-0.3 2.6l-4.4 -0.5l-2.6 1.3l1.5 4.7l-2.7 4.5l-2.9 1.9l-0.2 2.2l1.1 -0.1l-1.3 2.7l1.3 2.1l-1.5 2l-2.8 0.3l0.4 2.6l1.3 0.4l-0.6 1.9l4.2 0.9l-2 11.5l1.5 1.4l-3.6 2.4l-1 7l-3.3 4.8l0.8 3.1l-8.6 4.4l2 1.6l-1.3 4.9l4.2 2.8l3.6 0.2l4.6 -3.6l1.9 3.7l2.3 -2.9l-3.3 -2.2l2.9 -4l8 -5.3l6.2 -9.8l2 -0.7l10.1 8.1l13.5 0.8l0.7 2.1l-11.9 13.6l13.6 12.5l5.3 7.5l-6.8 5.3l-16.5 3.4l-4.9 0.2l-7 -3.2l-1.9 0.9l-13.9 16.9l-7.9 13.5l-2 0.5l-2.4 -1.4l-3.7 1.3l-2.3 -0.8l-3.6 -6.3l-9.4 -7.9l-0.7 -2.6l-9.4 0.2l-6 -2l0.2 -5.5l-3.2 -6.2l-0.9 1.4l-9.1 0.2l-2.8 3.1l0.8 2.7l-3.8 2.2l-0.7 2.3l-2.6 -0.4l-4.4 3.4l1.8 -4l-3.4 -3.8l0.8 -2.8l-1.5 -0.3l-0.6 -3l1.6 -0.9l0.9 -4.5l-8.7 0.3l-3.4 -5.8l0.3 -4.3l-3.8 -3.9l-8.9 -1.9l-2 -3.9l-2 -0.7l2.4 -1.6l-1.3 -5.7l4.7 -11.4l-4.9 -5.6l2.9 -4.5l-3.2 -4.3l0.1 -2l-3.8 -3.7l3.6 -3.5l-0.8 -1.9l1.9 -0.1l0 -1.3l1.4 3l3.5 -0.7l-0.2 3.4l6.1 0.9l2.5 -1.4l0.5 -2l3.3 0.8l1.4 -2.4l-1.7 -1.7l2.8 -2.6l-1.1 -3.1l-3.1 -1.1l0.5 -1.5l-2 -2.1l7.1 -2.4l4.2 -6.6z"/>
                        </a>
                        <a href="/region/rabat-sale-kenitra" class="jc-region" data-r="MA04">
                            <title>Rabat-Salé-Kénitra</title>
                            <path d="M647.6 104.9l3.2 0.8l4.3 -1.7l2.5 2.5l2.4 -1l4.6 3.3l7 0.6l0.5 12.6l2.6 0.2l0.7 1.7l3.7 -2.3l2.8 1.2l3.3 5.3l5.6 1.9l-0.3 4.6l5.9 1.3l-4.2 6.6l-7.1 2.4l2 2.1l-0.5 1.5l3.1 1.1l1.1 3.1l-2.8 2.6l1.7 1.7l-1.4 2.4l-3.3 -0.8l-0.5 2l-2.5 1.4l-6.1 -0.9l0.2 -3.4l-3.5 0.7l-1.4 -3l0 1.3l-1.9 0.1l0.8 1.9l-3.4 2.4l6.7 11.1l-2.8 4.8l4.8 5.3l-4.7 11.4l1.3 5.7l-2.4 1.6l2.1 0.9l-1.7 3.1l-0 4.1l2.3 1.7l-2.8 1.8l-5.7 -1.7l-7.1 1.7l-1.7 -2l-5.6 -0.5l0.6 -5.9l-8.2 -2.7l-3.1 10l1.5 4.2l-8.3 6.7l-3.2 -2.8l-10.2 -1.9l0 -12.8l-1.6 -3.2l3.7 -4l-0.9 -1.3l-7.5 -1.3l-2.2 2.5l-2.4 -4.5l-1.8 -9.3l-4.6 -4.4l9.2 -6.3l5.8 -6.7l0.9 0.7l-0.8 -0.9l5 -6.3l3 -7.9l2 -0.4l-1.9 0.2l8.6 -13.3l11.4 -25.1l1.1 0.1l-0.8 2.7l0.1 -1.2l1.9 0.2l-1 1.9l1.6 -1.7l-0.1 -1.4l-2.7 -0.8l3.1 -8.3z"/>
                        </a>
                        <a href="/region/beni-mellal-khenifra" class="jc-region" data-r="MA05">
                            <title>Béni Mellal-Khénifra</title>
                            <path d="M699.6 240l-3 3.9l-0.1 8.7l-2.2 9.5l-2.3 -0.6l-0.6 3.7l-6.6 0.7l0.1 5.7l3.1 1l-1.8 2.7l3.3 5.3l-6.2 -1.4l-6 1.6l-3.6 4.3l-5.1 0.8l-1.1 1.5l-1.2 7.7l4.5 2.8l-0.8 5l-5.7 2.5l-2.1 4.1l-2.7 1.9l-20 8l-12.4 8l-3.6 -0.5l-1.6 7.4l-14.5 0.6l-1.6 -3.1l-4.6 1.4l-1.6 -3.8l-1.5 0l2.7 -8l-3.1 -0.1l-1.5 -3l-2.2 -0.8l-0.3 -3.2l-2.5 0.2l-0.2 -1.4l3.3 -4.4l1.5 -0.6l3.1 1.9l6.4 -1.1l1.3 -0.8l-0.7 -3.2l1.8 0.3l-3.4 -5.4l0.5 -4.6l-3.1 -3.7l-0.8 -5.4l1.6 -11l-1.7 -0.8l4.9 -3.4l2.1 -6.1l-0.8 -5.3l1.5 -4.9l-3.6 -4l2.9 -9l-0.4 -6.5l2.1 -0.4l1.3 -2.4l4.1 -13.5l3.2 -1.8l1.3 1.3l5.7 0.1l7.3 4l1.2 -2.4l5.4 -2.4l1.2 -1.9l-1.5 -4.2l3.1 -10l8.2 2.7l-0.3 6.1l5.3 0.3l1.7 2l4 -0.1l1.6 -1.6l8 1.5l2 -1.6l-2.3 -1.7l0 -4.1l1.6 -2.8l2 3.4l8.9 1.9l3.8 3.9l-0.3 4.3l3.4 5.8l8.7 -0.3l-0.9 4.5l-1.6 0.9l0.6 3l1.5 0.3l-0.8 2.8l3.4 3.8l-1.8 4l1.1 -0.5z"/>
                        </a>
                        <a href="/region/casablanca-settat" class="jc-region" data-r="MA06">
                            <title>Casablanca-Settat</title>
                            <path d="M499.6 249.2l17.1 -16.7l5.4 -7.7l0.6 -1.4l-1.1 0.3l1.5 -0.3l-0.8 -2l5.3 -5.9l4.2 0.5l7.7 -7.3l25 -10.7l1.2 0.5l7.8 -5.3l3.2 -0.5l-1.3 1.3l4.2 -1.2l7.9 -7.2l-0.7 1.4l2.3 -0.1l6.7 -5.7l5.4 -1.8l4.6 4.4l1.8 9.3l2.4 4.5l2.2 -2.5l7.5 1.3l0.9 1.3l-3.7 4l1.6 3.2l-0.2 11.8l-3.6 2.4l-3.9 13.2l-1.3 2.4l-2.1 0.4l0.4 6.5l-2.9 9l3.6 4l-1.5 4.9l0.8 5.3l-2.1 6.1l-4.9 3.4l-3 -0.7l-4.8 -4.5l-2.8 -0.1l-2.2 -3.8l-5.1 1.5l-0.8 -1.6l-3 1.3l-4.2 -1.4l-5 -5.6l0.2 -2.3l-3.1 -0.6l-7 -5.4l-4.5 -7.7l-2.2 1.5l-1.9 7.1l1 6.5l-3.3 4.4l-10.4 5.2l-2.7 3.5l-3.6 0.3l-6.4 3.7l0.5 -2.1l-4.8 -10l-2.6 -2.1l-8.6 2l-4.2 -1.8l-4.6 -5.3l2.1 -4.1l-4.2 -3z"/>
                        </a>
                        <a href="/region/marrakech-safi" class="jc-region" data-r="MA07">
                            <title>Marrakech-Safi</title>
                            <path d="M598.1 329.4l-6.3 6.8l-6.9 1.7l-3.3 2.4l-4.5 0.4l-0.7 2.1l-5.2 3l-0.8 1.9l-5.9 -2.2l0.8 1.3l-4.1 2.4l-3.8 10.2l-2.9 1.9l-6.4 -1.8l-12 4.6l-15.6 -2.3l-3.5 2.7l-0.8 2.8l-2 -7.7l-2.4 -2.3l-8.4 5.2l-0.5 3.9l-1.3 0.4l-7.7 -0.5l-4.1 1.5l-4.7 -5.2l-0.6 2l-3.1 -0.4l-2 1.4l-4.7 -1.5l-0.5 -1.3l-1.4 2.7l-1.9 -1l-1.8 3.5l-4 0.5l-1 -5l-4.8 -5.5l1 -2.8l-1.7 -8.3l2.3 -11.4l-0.8 -3.1l-1.7 -0.7l3.7 -7.7l4.5 -5.4l0.6 -5.9l17.3 -20.5l1.3 -5.2l3.2 -3.7l0.9 -8l-2.5 -3.7l3.1 -7.3l-2.8 -3.9l12.5 -11.2l3.6 3l-2.1 4.1l4.6 5.3l4.2 1.8l8.6 -2l1.3 0.8l6.4 13.5l1.8 -2.2l7.4 -1.9l2.7 -3.5l10.4 -5.2l3.3 -4.4l-1 -6.5l1.6 -6.4l2.7 -2.1l4.3 7.6l7 5.4l3.1 0.6l-0.2 2.3l5 5.6l4.2 1.4l3 -1.3l0.8 1.6l5.1 -1.5l2.2 3.8l2.8 0.1l4.8 4.5l2.5 -0.3l2.2 1.8l-1.6 11l0.8 5.4l3.1 3.7l-0.5 4.6l3.4 5.4l-1.8 -0.3l0.7 3.2l-1.3 0.8l-6.4 1.1l-3.1 -1.9l-1.5 0.6l-3.3 4.4l0.2 1.4l2.5 -0.2l0.3 3.2l2.2 0.8l1.5 3l3.1 0.1l-2.7 8z"/>
                        </a>
                        <a href="/region/draa-tafilalet" class="jc-region" data-r="MA08">
                            <title>Drâa-Tafilalet</title>
                            <path d="M636.8 443l2.3 -4.9l-1 -3.1l1.8 -2.8l-2.2 -13.8l4.1 -3.5l-2.3 -1l-4.3 1.3l-1.5 -3l1 -3.3l-5.3 -6.1l4.1 -3.7l0 -1.6l-6.7 -6l-1 -2.8l-8 2.3l-1.2 -2.3l-1.5 0.2l0.3 5.9l-1.4 1.8l-2.2 -1.5l-5.2 1.3l-7.2 -2.6l-7.1 10l-0.3 6.7l-3.6 -5.3l-2.8 0.2l-2.5 -1.6l-1 0.9l-2.5 -1.7l-4.7 0.8l2.3 -2.1l0.2 -2.1l-2.6 -7.5l-2.7 1.6l-0.3 -4.3l8.9 -5.7l-2.8 -6.2l-0.1 -5.2l-7.3 -0.7l2.1 -1.9l0.1 -4.1l2 -1.4l-5.4 0.1l1 -10.9l-1.6 -5.5l7.5 -4.9l0.9 -2.3l4.5 -0.4l3.3 -2.4l6.9 -1.7l6.3 -6.8l1.5 -0l1.6 3.8l4.6 -1.4l1.6 3.1l14.5 -0.6l1.6 -7.4l3.6 0.5l8 -5.8l1.6 0.5l2.8 -2.7l21.9 -9.2l2.9 -4.8l5.7 -2.5l0.8 -5l-4.5 -2.8l2.3 -9.2l5.1 -0.8l3.6 -4.3l6 -1.6l6.2 1.4l-3.3 -5.3l1.8 -2.7l-3.1 -1l-0.1 -5.7l6.6 -0.7l0.6 -3.7l2.3 0.6l2.2 -9.5l0.3 -9.1l6.1 -6.4l2.6 0.4l0.7 -2.3l3.8 -2.2l-0.8 -2.7l3.9 -3.7l8 0.4l0.9 -1.4l3.2 6.2l-0.2 5.5l6 2l9.4 -0.2l0.7 2.6l9.4 7.9l3.6 6.3l10.4 0.4l5.2 4.6l-0.7 3.4l5.8 8l0.5 5.9l3.1 -0.3l-0 3.7l9 0.2l-3.3 3.8l1.6 1.7l5.5 -2.3l9.3 2.5l7.4 -4.4l-3.6 5.3l-0.4 7.5l4 9.8l4.2 -1.7l-7.3 7.8l-21.7 5l0 14.3l1 0.4l-4.2 3.3l-1.2 -1.2l-1.9 4.2l1.7 0.6l-1.7 2.8l1.6 3.5l-0.9 3.1l2.2 0.8l1.5 -3.7l1.7 1.4l-0.3 3.7l1.7 0l0.2 -4.7l0.1 3.3l1.1 -0.3l2 4.4l1.5 -1l0.8 4.7l-0.8 3.4l-5.2 5.2l1 6.4l-2 5.1l-1.2 -1.2l-7.9 3.2l-16.3 2.7l-9.1 3.4l-14.6 14.7l-17.6 8.9l-17.8 14.2l-2.8 4.1l-0.3 4.3l-12.3 17l-3.9 -0.7l-2.3 -2.5l-3 1.5l1.1 -4.4l-3.6 -1.6l-14.3 3.1l-7.1 -1.3l-13.2 1.1z"/>
                        </a>
                        <a href="/region/souss-massa" class="jc-region" data-r="MA09">
                            <title>Souss-Massa</title>
                            <path d="M520.1 518.4l-15.8 -20l-1.6 -4.1l-7.1 -0.8l-3.7 -4.1l1.3 -3.5l-1.8 -0.4l-0.6 -2.1l-1 -10.5l3.7 -1.2l-1.8 2.3l0.7 0.9l1.3 -0.9l1.7 -1.8l-1.1 -1.8l3.6 -2.6l0.3 -1.8l-3.4 -6.7l-0.4 -5.4l-3.1 -4.1l-5.9 -3.3l-2 0.4l-3 4.2l-3.5 -1.7l-0.5 2.3l-3.2 -3.9l-2.2 3.1l-2.6 -2l1.1 2.6l-1.5 1.5l0.2 -1.9l-2.9 -2.1l-4.3 2.5l-4.6 -0.7l-1.7 -2.7l1.3 -4.6l-5.5 3.1l-0.8 -7.9l9.4 -10.4l3.7 -6.4l5.5 -12.4l2.9 -16.5l-0.6 -1.9l-2.1 -0.2l-2.8 -7.2l-1.9 -0.5l-2.5 -3.7l-5.4 -1.1l4.5 -11.4l-1.2 -1.9l0.5 -5.6l2.5 0.7l2.9 8.3l1.4 0.4l2.6 -0.9l1.8 -3.5l1.9 1l1.4 -2.7l0.5 1.3l4.7 1.5l2 -1.4l3.1 0.4l0.6 -2l4.7 5.2l4.1 -1.5l7.7 0.5l1.3 -0.4l0.5 -3.9l8.4 -5.2l2.4 2.3l2.1 7.7l0.7 -2.8l3.5 -2.7l15.6 2.3l12 -4.6l6.4 1.8l1.9 -0.8l4.8 -11.3l4.1 -2.4l-0.8 -1.3l2.2 0.3l2.7 1.7l-1.1 2.1l2 3.8l-1 10.9l5.4 -0.1l-2 1.4l-0.1 4.1l-2.1 1.9l7.3 0.7l0.1 5.2l2.8 6.2l-8.9 5.7l0.3 4.3l2.7 -1.6l2.6 7.5l-0.2 2.1l-2.3 2.1l4.7 -0.8l2.5 1.7l1 -0.9l2.5 1.6l2.8 -0.2l3.6 5.3l0.3 -6.7l7.1 -10l7.2 2.6l5.2 -1.3l2.2 1.5l1.4 -1.8l-0.3 -5.9l1.5 -0.2l1.2 2.3l8 -2.3l1 2.8l6.7 6l0 1.6l-4.1 3.7l5.3 6.1l-1 3.3l1.5 3l4.3 -1.3l2.3 1l-4.1 3.5l2.2 13.8l-1.8 2.8l1 3.1l-2.3 4.9l-3.9 2.8l-8.7 0.3l-5 3.2l-17.7 -3.8l-5.7 2.3l-8 6.4l-12.3 0.8l-1.8 3.8l-13.8 7.3l-2.5 3.2l-13.9 8.4l-11 10.6l-12.4 6l0 24.1z"/>
                        </a>
                        <a href="/region/guelmim-oued-noun" class="jc-region" data-r="MA10">
                            <title>Guelmim-Oued Noun</title>
                            <path d="M515.5 587.5l-6.2 -2.1l-5.1 -5.6l-16.7 -11.5l-13.4 4.1l-11.5 -7.9l-5.2 -7.5l-2 -0.7l-0.8 1.5l-3.7 0.6l-3.8 -4.2l-2.8 0.5l-7.2 -6.4l-24.7 2.4l-3.6 -1.1l-6 1.8l-15.2 1.1l-9 -3.4l-6.5 -0.7l-5.3 -3.5l-6.6 -15.3l-3.7 -5.2l14.5 -5.9l7.8 -10.4l14.9 -15.5l29.4 -17l9.8 -12.7l5.9 -4.6l10.8 -19.2l0.9 7.5l5.5 -3.1l-1.3 4.6l3.5 3.7l3.4 -0.5l2.9 -2.4l4.7 4l-0.8 -4l2.6 2l2.2 -3.1l3.2 3.9l0.5 -2.3l3.5 1.7l3 -4.2l4.7 0.2l6.3 6.8l0.4 5.4l3.4 6.7l-0.3 1.8l-3.6 2.6l1.1 1.8l-1.7 1.8l-1.3 0.9l-0.7 -0.9l1.8 -2.3l-3.7 1.2l1 10.5l0.6 2.1l1.8 0.4l-1.3 3.5l3.7 4.1l7.1 0.8l1.6 4.1l15.8 20l0.1 61.4l-0.8 -22.7l-7.1 0l2.3 11.9l-1.2 9l2.1 9.5z"/>
                        </a>
                        <a href="/region/laayoune-sakia-el-hamra" class="jc-region" data-r="MA11">
                            <title>Laâyoune-Sakia El Hamra</title>
                            <path d="M515.5 587.5l3.9 0.5l-0.3 66.9l-174.9 0.3l0 93.3l-13 -2.9l-18.3 3.2l-14.5 -5.7l-12.2 -2.6l-9.2 0.8l-20.7 7.1l-20.4 -5.4l-12 0.6l-10.6 3.3l-3 -0.9l-5.3 -5.4l-6.3 -1.7l-6.3 -7.4l4 -13.1l0 -22.5l2.1 -6.9l4.8 -7.5l3.1 -13.1l3.8 -4.7l3.5 -8l-0.1 -8.3l1.4 -3.1l3.6 -4.7l4.7 -1.2l3.7 -3.1l3.2 -5.2l11.2 -2.9l16.2 -10.6l5 -4.9l7.9 -20.2l0.5 -5l5.5 -9.4l7.3 -21.9l6 -3.4l3.7 -8.6l3.4 -3.6l18.1 -2.3l25.7 -5.5l15.8 -7.4l3.7 5.2l6.6 15.3l5.3 3.5l6.5 0.7l9 3.4l15.2 -1.1l6 -1.8l3.6 1.1l24.7 -2.4l7.2 6.4l2.8 -0.5l3.8 4.2l3.7 -0.6l0.8 -1.5l2 0.7l5.2 7.5l11.5 7.9l13.4 -4.1l16.7 11.5l5.1 5.6l6.2 2.1z"/>
                        </a>
                        <a href="/region/dakhla-oued-ed-dahab" class="jc-region" data-r="MA12">
                            <title>Dakhla-Oued Ed Dahab</title>
                            <path d="M192.4 731.5l6.3 7.4l6.3 1.7l5.3 5.4l3 0.9l10.6 -3.3l12 -0.6l20.4 5.4l20.7 -7.1l9.2 -0.8l12.2 2.6l14.5 5.7l18.3 -3.2l13 2.9l-0.2 53.4l-18 8l-13.5 2.8l-20.8 14.5l-7.2 11.3l-0.2 7.5l3.3 11l4.1 65.6l-206.7 0.2l-6.1 22.4l0.7 9.3l-2.9 -4.1l1.5 -2.4l-1 -2l1.7 -4.3l1.7 -19.2l2.6 -12.4l-0.1 -8.5l1.8 -2.9l-0.5 -3.8l3.1 -4l4.6 -14.4l2.3 -1.8l-0.7 -1.1l5.3 -5.3l3.2 1.6l6.1 -2.7l4 -11.8l3.6 -3.1l3.7 -18.6l4.2 -0.9l3.5 -5l-0.7 -3.8l-1.2 -1l-1.6 0.8l0.3 -1.1l4.2 -5.3l3.1 -8l4.3 -4.7l1.7 -3.7l-1.3 -1.9l3.7 -3.2l10.4 -15.1l-2.2 0.5l1 -3.1l-1.1 -2.4l-1.1 1.4l0.2 -1.8l-5.7 5.5l-4.2 8.9l0.9 0.5l-1.4 -0.2l-0.9 2.2l-1.2 -1.2l8.5 -13.6l13 -8.9l1.2 -3.1l13.1 -12.3l5.3 -7.5l4 -3.6l6.9 -2.9l1.6 -4.7l4.3 -3z"/>
                        </a>
                    </svg>
                    <ul class="jc-maplist">
                        <li><a href="/region/tanger-tetouan-al-hoceima" data-r="MA01">Tanger-Tétouan-Al Hoceima</a></li>
                        <li><a href="/region/l-oriental" data-r="MA02">L'Oriental</a></li>
                        <li><a href="/region/fes-meknes" data-r="MA03">Fès-Meknès</a></li>
                        <li><a href="/region/rabat-sale-kenitra" data-r="MA04">Rabat-Salé-Kénitra</a></li>
                        <li><a href="/region/beni-mellal-khenifra" data-r="MA05">Béni Mellal-Khénifra</a></li>
                        <li><a href="/region/casablanca-settat" data-r="MA06">Casablanca-Settat</a></li>
                        <li><a href="/region/marrakech-safi" data-r="MA07">Marrakech-Safi</a></li>
                        <li><a href="/region/draa-tafilalet" data-r="MA08">Drâa-Tafilalet</a></li>
                        <li><a href="/region/souss-massa" data-r="MA09">Souss-Massa</a></li>
                        <li><a href="/region/guelmim-oued-noun" data-r="MA10">Guelmim-Oued Noun</a></li>
                        <li><a href="/region/laayoune-sakia-el-hamra" data-r="MA11">Laâyoune-Sakia El Hamra</a></li>
                        <li><a href="/region/dakhla-oued-ed-dahab" data-r="MA12">Dakhla-Oued Ed Dahab</a></li>
                    </ul>
                </div>
            </div>
        </section>

        <#-- ==================== CTA EMPLOYEUR ==================== -->
        <section class="add-job jc-cta">
            <div class="jc-wrap jc-cta-inner">
                <div class="jc-cta-text">
                    <h2>Vous êtes un <span>employeur</span> ?</h2>
                    <p>
                        Publiez votre annonce et touchez des milliers de candidats au Maroc.
                        Indiquez les compétences nécessaires pour le poste, c'est gratuit.
                    </p>
                    <a href="/ajouter-offre-emploi" class="jc-btn jc-btn-primary jc-btn-lg">Ajouter une offre</a>
                </div>
                <img src="/assets/img/add-job.png" alt="" class="jc-cta-img" />
            </div>
        </section>

        <#include "/common/footer.ftl">
        <script src="/assets/js/script.js" defer></script>
        <script src="/assets/js/temp/index.js" defer></script>
        <script>
            window.citiesData = [
            <#list data.cities as city>
                {
                slug: "${city.slug?js_string}",
                regionSlug: "${(city.region.slug)!''?js_string}"
                }<#if city_has_next>,</#if>
            </#list>
            ];
            window.domainsData = [
            <#list data.domains as domain>
                { slug: "${domain.slug?js_string}" }<#if domain_has_next>,</#if>
            </#list>
            ];
        </script>


        <#-- Carte des régions : survol lié entre la carte et la liste. -->
        <script>
        document.addEventListener('DOMContentLoaded', function () {
            var wrap = document.querySelector('.jc-mapwrap');
            if (!wrap) { return; }
            function link(on) {
                return function (e) {
                    var t = e.target.closest('[data-r]');
                    if (!t) { return; }
                    var r = t.getAttribute('data-r');
                    Array.prototype.forEach.call(
                        wrap.querySelectorAll('[data-r="' + r + '"]'),
                        function (el) { el.classList.toggle('is-hot', on); }
                    );
                };
            }
            wrap.addEventListener('mouseover', link(true));
            wrap.addEventListener('mouseout', link(false));
        });
        </script>
    </body>
</html>
