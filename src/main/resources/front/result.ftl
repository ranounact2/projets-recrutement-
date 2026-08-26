<!DOCTYPE html>
<html lang = "en">
    <head>
        <#include "/common/head.ftl">
        <link rel="stylesheet" href="/assets/css-min/style.min.css" defer/>
        <link rel="stylesheet" href="/assets/css-min/result.min.css" defer/>
    <#if zones??>
        <#list zones as zonePage>
            <#if zonePage.zones??>
                <#list zonePage.zones?keys as zoneKey>
                    <#assign currentZone = zonePage.zones[zoneKey]>
                    <#if currentZone.values??>
                        <#-- Meta description -->
                        <#if currentZone.values.meta_description??>
                            <meta name="description" content="${currentZone.values.meta_description.content!currentZone.values.meta_description.value!''}">
                        </#if>
                        <#-- Meta keywords -->
                        <#if currentZone.values.meta_keyword??>
                            <meta name="keywords" content="${currentZone.values.meta_keyword.content!currentZone.values.meta_keyword.value!''}">
                        </#if>
                        <#-- Title -->
                        <#if currentZone.values.title??>
                            <title>${currentZone.values.title.content!currentZone.values.title.value!''}</title>
                        </#if>
                    </#if>
                </#list>
            </#if>
        </#list>
    </#if>
    </head>
    <body>
        <#include "/common/header.ftl">
        <#assign h1Content = "Découvrez les différents domaines d'emploi au Maroc">
        <#assign h1BlockContent = "Explorez une variété de secteurs et trouvez des opportunités de carrière adaptées à vos compétences et intérêts.">
        <#assign h2CategorieContent = "">
        <#assign textBlockCategorie = "">

        <#if zones??>
            <#list zones as zonePage>
                <#if zonePage.zones??>
                    <#list zonePage.zones?keys as zoneKey>
                        <#assign currentZone = zonePage.zones[zoneKey]>

                        <#-- Récupération du h1 et h1_block -->
                        <#if currentZone.values?? && currentZone.values.h1??>
                            <#assign h1Content = currentZone.values.h1.content!currentZone.values.h1.value!''>
                        </#if>
                        <#if currentZone.values?? && currentZone.values.h1_block??>
                            <#assign h1BlockContent = currentZone.values.h1_block.content!currentZone.values.h1_block.value!''>
                        </#if>

                        <#-- Récupération du h2_block_categorie -->
                        <#if currentZone.values?? && currentZone.values.h2_block_categorie??>
                            <#assign h2CategorieContent = currentZone.values.h2_block_categorie.content!currentZone.values.h2_block_categorie.value!''>
                        </#if>

                        <#-- Récupération du text_block_categorie -->
                        <#if currentZone.values?? && currentZone.values.text_block_categorie??>
                            <#assign textBlockCategorie = currentZone.values.text_block_categorie.content!currentZone.values.text_block_categorie.value!''>
                        </#if>
                    </#list>
                </#if>
            </#list>
        </#if>
<#--        <#if (!data?? || !data.domains?? || data.domains?size == 0) && !allRegion?? && !dataAdsAndRegion?? && !data.cities?? && !allCities??>-->
<#--            <section class="intro">-->
<#--                <h1>${h1Content}</h1>-->
<#--                <p>${h1BlockContent}</p>-->
<#--            </section>-->
<#--        </#if>-->
            <#if !resultat??>
                <#include "../common/breadcrumb.ftl" />
            </#if>

            <#-- Move intro text and cities to top for domain+region selection -->
            <#if dataAdsAndRegion??>
                <section class="sectionTitre">
                    <h1>${h1Content}</h1>
                    <p>${h1BlockContent}</p>
                </section>
                <section class="regions">
                    <div class="regions-grid">
                        <#list dataAdsAndRegion.cities as city>
                            <#if city??>
                                <a href="/categorie/${domainSlug}/${regionSlug}/${city.slug}">
                                    <div class="region">${city.name}</div>
                                </a>
                            </#if>
                        </#list>
                    </div>
                </section>
            </#if>
        <#if resultat??>
            <section class="intro">
                <form action="">
                    <div>
                        <svg xmlns="http://www.w3.org/2000/svg" width="27" height="27" viewBox="0 0 27 27" fill="none">
                            <g clip-path="url(#clip0_1_726)">
                                <path
                                d="M22.5 5.625H4.5C3.2625 5.625 2.26125 6.6375 2.26125 7.875L2.25 19.125C2.25 20.3625 3.2625 21.375 4.5 21.375H22.5C23.7375 21.375 24.75 20.3625 24.75 19.125V7.875C24.75 6.6375 23.7375 5.625 22.5 5.625ZM12.375 9H14.625V11.25H12.375V9ZM12.375 12.375H14.625V14.625H12.375V12.375ZM9 9H11.25V11.25H9V9ZM9 12.375H11.25V14.625H9V12.375ZM7.875 14.625H5.625V12.375H7.875V14.625ZM7.875 11.25H5.625V9H7.875V11.25ZM18 19.125H9V16.875H18V19.125ZM18 14.625H15.75V12.375H18V14.625ZM18 11.25H15.75V9H18V11.25ZM21.375 14.625H19.125V12.375H21.375V14.625ZM21.375 11.25H19.125V9H21.375V11.25Z"
                                fill="#696969"/>
                            </g>
                            <defs>
                                <clipPath id="clip0_1_726">
                                    <rect width="27" height="27" fill="white"/>
                                </clipPath>
                            </defs>
                        </svg>
                        <input class="inpt" id="keyword" type="text" placeholder="Titre, Mots clés ou phrase">
                    </div>
                    <div>
                        <svg xmlns="http://www.w3.org/2000/svg" width="27" height="27" viewBox="0 0 27 27" fill="none">
                            <g clip-path="url(#clip0_1_710)">
                                <path
                                d="M13.5 2.25C9.14625 2.25 5.625 5.77125 5.625 10.125C5.625 16.0312 13.5 24.75 13.5 24.75C13.5 24.75 21.375 16.0312 21.375 10.125C21.375 5.77125 17.8538 2.25 13.5 2.25ZM13.5 12.9375C11.9475 12.9375 10.6875 11.6775 10.6875 10.125C10.6875 8.5725 11.9475 7.3125 13.5 7.3125C15.0525 7.3125 16.3125 8.5725 16.3125 10.125C16.3125 11.6775 15.0525 12.9375 13.5 12.9375Z"
                                fill="#696969"/>
                            </g>
                            <defs>
                                <clipPath id="clip0_1_710">
                                    <rect width="27" height="27" fill="white"/>
                                </clipPath>
                            </defs>
                        </svg>
                        <select name="" id="city">
                            <option value="" <#if !citySlug?? || citySlug == "">selected</#if>>toutes les villes</option>
                            <#list resultat.cities as city>
                                <option value="${city.slug}" <#if citySlug?? && city.slug == citySlug>selected</#if>>${city.name}</option>
                            </#list>
                        </select>
                    </div>
                    <div>
                        <svg xmlns="http://www.w3.org/2000/svg" width="27" height="27" viewBox="0 0 27 27" fill="none">
                            <g clip-path="url(#clip0_1_723)">
                                <path
                                d="M11.25 18V16.875H3.38625L3.375 21.375C3.375 22.6238 4.37625 23.625 5.625 23.625H21.375C22.6238 23.625 23.625 22.6238 23.625 21.375V16.875H15.75V18H11.25ZM22.5 7.875H17.9888V5.625L15.7388 3.375H11.2388L8.98875 5.625V7.875H4.5C3.2625 7.875 2.25 8.8875 2.25 10.125V13.5C2.25 14.7487 3.25125 15.75 4.5 15.75H11.25V13.5H15.75V15.75H22.5C23.7375 15.75 24.75 14.7375 24.75 13.5V10.125C24.75 8.8875 23.7375 7.875 22.5 7.875ZM15.75 7.875H11.25V5.625H15.75V7.875Z"
                                fill="#696969"/>
                            </g>
                            <defs>
                                <clipPath id="clip0_1_723">
                                    <rect width="27" height="27" fill="white"/>
                                </clipPath>
                            </defs>
                        </svg>
                        <select name="" id="domain">
                            <option value="" <#if !domainSlug?? || domainSlug == "">selected</#if>>tous les domaines</option>
                            <#list resultat.domains as domain>
                                <option value="${domain.slug}" <#if domainSlug?? && domain.slug == domainSlug>selected</#if>>${domain.name}</option>
                            </#list>
                        </select>
                    </div>
                    <button id="linkButton" type="button">
                        <img src="/assets/img/research.png" alt="">
                    </button>
                </form>
            </section>
            <div>
                <#include "../common/breadcrumb.ftl" />
            </div>
            <section class="sectionTitre">
                <h1>${h1Content}</h1>
                <p>${h1BlockContent}</p>
            </section>
            
            <#-- 1. AFFICHAGE DES OFFRES EN PREMIER (page de résultats) -->
            <#if resultat.jobs?? && resultat.jobs?has_content>
                <section class = "offres">
                    <div class="table-offre">
                    <#list resultat.jobs as job>
                        <div class="offre">
                            <#if job.img??>
                                <img src="/assets/img/${job.img}" alt="company logo">
                            <#else>
                                <img src="/assets/img/offer-1.png" alt="company logo">
                            </#if>
                            <div>
                                <#if job.title??>
                                    <a class="titreOffre" href="/offre-emploi-maroc/${job.key}" title="${job.title}">${job.title}</a>
                                </#if>
                                <br><br>
                                <span><img src="/assets/img/place.png" alt="city">${job.city!''}</span>
                                <span><img src="/assets/img/business_center.png" alt="type">${job.type!''}</span>
                                <span class="orange">${job.type!''}</span>
                            </div>
                            <div>
                                <#if job.type?? && job.type?has_content>
                                    <span class="badge">${job.type}</span>
                                </#if>
                                <#if job.experienceLevel??>
                                    <b>${job.experienceLevel}</b>
                                </#if>
                            </div>
                        </div>
                    </#list>
                    <#if resultat.totalPages?? && resultat.totalPages gt 1>
                        <div class="pagination">
                            <#if resultat.pageNumber gt 1>
                                <a href="?page=${resultat.pageNumber -1}&size=${resultat.pageSize}" class="pagination-link">Previous</a>
                            </#if>
                            <#if resultat.pageNumber gt 1>
                                <span class="pagination-link"><a href="?page=${resultat.pageNumber -1}&size=${resultat.pageSize}" class="pagination-link">${resultat.pageNumber-1}</a></span>
                            </#if>
                            <span class="pagination-link selected current-page"><a href="?page=${resultat.pageNumber}&size=${resultat.pageSize}" class="pagination-link">${resultat.pageNumber}</a></span>
                            <#if resultat.pageNumber lt resultat.totalPages>
                                <span class="pagination-link"><a href="?page=${resultat.pageNumber +1}&size=${resultat.pageSize}" class="pagination-link">${resultat.pageNumber +1}</a></span>
                                <a href="?page=${resultat.pageNumber +1}&size=${resultat.pageSize}" class="pagination-link">Next</a>
                            </#if>
                        </div>
                    </#if>
                </section>
            <#else>
                <#include "../common/no-results.ftl" />
            </#if>
            
            <#-- 2. AFFICHAGE DES RÉGIONS EN DEUXIÈME -->
            <#if data?? && data.regions??>
                <section class="regions">
                    <h2 class="mb-2">Les régions qui recrutent</h2>
                    <p class="subtitle">
                        Explorez les régions du Maroc où les opportunités d'emploi sont les plus nombreuses.
                    </p>
                    <div class="regions-grid">
                        <#list data.regions as region>
                            <#if region??>
                                <a href="/categorie/${domainSlug}/${region.slug}">
                                    <div class="region">${region.name}</div>
                                </a>
                            </#if>
                        </#list>
                    </div>
                </section>
            </#if>
            
            <#-- 3. AFFICHAGE DES VILLES EN TROISIÈME -->
            <#if data?? && data.cities??>
                <section class="city">
                    <div class="container">
                        <div class="site-heading">
                            <h2>Les <span>villes</span> qui recrutent</h2>
                            <br>
                            <p style="font-weight: bolder;">Découvrez les offres d'emploi dans les villes avec le plus grand nombre d'opportunités d'emploi.</p>
                        </div>
                        <div class="cities">
                            <div class="row">
                                <#list data.cities?chunk(3) as cityGroup>
                                    <div class="column city-column" style="<#if (cityGroup?counter > 5) >display: none;</#if>">
                                        <div class="city-list">
                                            <ul>
                                                <#list cityGroup as city>
                                                    <#if city.region?? && city.region.slug??>
                                                        <li>
                                                            <a href="/categorie/${domainSlug}/${city.region.slug}/${city.slug}">${city.name}</a>
                                                        </li>
                                                    </#if>
                                                </#list>
                                            </ul>
                                        </div>
                                    </div>
                                </#list>
                            </div>
                        </div>
                    </div>
                </section>
            </#if>
        </#if>
        <br>

        <#-- Affichage  Domains -->
        <#if data?? && data.domains?? && !resultat?? && !allRegion?? && !dataAdsAndRegion?? && !allCities??>
            <section class="sectionTitre">
                <h1>${h1Content}</h1>
                <p>${h1BlockContent}</p>
            </section>
            <section class="domaine">
                <div class="table-domain">
                    <#list data.domains as this>
                        <div class="col">
                            <#if regionSlug?? && citySlug??>
                                <a href="/categorie/${this.slug}/${regionSlug}/${citySlug}" title="${this.name}">
                                    <#if this.icon??>
                                        <i id="large" class="${this.icon}"></i>
                                    <#else>
                                        <div class="default-icon-class" id="large" style="color: #8b91dd;"></div>
                                    </#if>
                                    <br>
                                    ${this.name}
                                </a>
                            <#else>
                                <a href="/categorie/${this.slug}" title="${this.name}">
                                    <#if this.icon??>
                                        <i id="large" class="${this.icon}"></i>
                                    <#else>
                                        <div class="default-icon-class" id="large" style="color: #8b91dd;"></div>
                                    </#if>
                                    <br>
                                    ${this.name}
                                </a>
                            </#if>
                        </div>
                    </#list>
                </div>
            </section>
            <#-- Section supprimée : Les offres d'emploi ne doivent pas être affichées dans les pages de liens -->
        </#if>
            <#-- Affichage  region-->
        <#if allRegion??>
            <section class="regions">
                <section class="sectionTitre">
                    <h1>${h1Content}</h1>
                    <p>${h1BlockContent}</p>
                </section>
                <div class="regions-grid">
                    <#list allRegion as region>
                        <#if region??>
                            <a href="/region/${region.slug}">
                                <div class="region">${region.name}</div>
                            </a>
                        </#if>
                    </#list>
                </div>
            </section>
        </#if>
            <#-- Section supprimée : Les offres d'emploi ne doivent pas être affichées dans les pages de liens -->

        <#if jobs??>
            <section class="links">
                <#if zp??>
                    <div class="site-heading">
                        <h2 style="font-weight: bolder;">${zp.zones.zone3.values.h1.value!''}</h2>
                        <p>${zp.zones.zone3.values.h1_block.value!''}</p>
                        <h2 style="font-weight: bolder;">${zp.zones.zone3.values.h2.value!''}</h2>
                        <p>${zp.zones.zone3.values.h2_block.value!''}</p>
                    </div>
                </#if>
                <div class="back-color row-gap row-gap-block">
                    <#list jobs.cities as city>
                        <div class="col-3-flex">
                            <a class="list-link" href="<#if domainSlug??>
                    /categorie/${domainSlug}/${city.region.slug}/${city.slug}
                    <#else>
                    /region/${regionSlug}/${city.slug}
                    </#if>">
                                <b>${city.name}</b>${city.population}
                            </a>
                        </div>
                    </#list>
                </div>
            </section>
        </#if>

            <#-- Section supprimée : Les offres d'emploi ne doivent pas être affichées dans les pages de liens -->
            <#if dataAdsAndRegion?? && (!dataAdsAndRegion.ads?? || !dataAdsAndRegion.ads?has_content)>
                <#include "../common/no-results.ftl" />
            </#if>
<#--        region and domain-->
            <#if allCities??>
                  <section class="regions">
                    <section class="sectionTitre">
                        <h1>${h1Content}</h1>
                        <p>${h1BlockContent}</p>
                    </section>
                    <div class="regions-grid">
                        <#list allCities as city>
                            <#if city??>
                                <a href="/region/${regionSlug}/${city.slug}">
                                    <div class="region">${city.name}</div>
                                </a>
                            </#if>
                        </#list>
                    </div>
                </section>
                <section class="domaine">
                    <div class="table-domain">
                        <#list domainAds as this>
                            <div class="col">
                                <#if regionSlug??>
                                    <a href="/categorie/${this.slug}/${regionSlug}" title="${this.name}">
                                        <#if this.icon??>
                                            <i id="large" class="${this.icon}"></i>
                                        <#else>
                                            <div class="default-icon-class" id="large" style="color: #8b91dd;"></div>
                                        </#if>
                                        <br>
                                        ${this.name}
                                    </a>
                                <#else>
                                    <a href="/categorie/${this.slug}" title="${this.name}">
                                        <#if this.icon??>
                                            <i id="large" class="${this.icon}"></i>
                                        <#else>
                                            <div class="default-icon-class" id="large" style="color: #8b91dd;"></div>
                                        </#if>
                                        <br>
                                        ${this.name}
                                    </a>
                                </#if>
                            </div>
                        </#list>
                    </div>
                </section>
            </#if>
        <footer>
            <#include "../common/footer.ftl" />
        </footer>

            <script src="/assets/js/script.js" defer></script>
            <script src="/assets/js/temp/index.js" defer></script>
            <script>
                <#if resultat??>
                window.citiesData = [
                <#list resultat.cities as city>
                    {
                    slug: "${city.slug?js_string}",
                    regionSlug: "${(city.region.slug)!''?js_string}"
                    }<#if city_has_next>,</#if>
                </#list>
                ];
                window.domainsData = [
                <#list resultat.domains as domain>
                    { slug: "${domain.slug?js_string}" }<#if domain_has_next>,</#if>
                </#list>
                ];
                <#else>
                // resultat n'est pas disponible sur cette page (pages de liens)
                window.citiesData = [];
                window.domainsData = [];
                </#if>
            </script>
    </body>
</html>
