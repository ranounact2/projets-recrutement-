<!DOCTYPE html>
<html lang="en">
    <head class="head-section">
        <link rel="stylesheet" href="${assetsPath}/css-min/index.min.css" defer/>
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
    <#include "/common-new/temp/head.ftl">
    <body class="home-page">
        <#include "/common-new/temp/header.ftl">

        <section class="intro">
            <#-- H1 depuis les zones ou valeur par défaut -->
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

            <#-- Paragraphe depuis les zones ou valeur par défaut -->
            <#assign paragraphContent = "Votre nouvel emploi vous attend">
            <#if zones??>
                <#list zones as zonePage>
                    <#if zonePage.zones??>
                        <#list zonePage.zones?keys as zoneKey>
                            <#assign currentZone = zonePage.zones[zoneKey]>
                            <#if currentZone.values?? && currentZone.values.paragraph??>
                                <#assign paragraphContent = currentZone.values.paragraph.content!currentZone.values.paragraph.value!''>
                                <#break>
                            </#if>
                        </#list>
                    </#if>
                </#list>
            </#if>
            <p>${paragraphContent}</p>

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
                        <option value="" selected>toutes les villes</option>
                        <#list data.cities as city>
                            <option value="${city.slug}">${city.name}</option>
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
                        <option value="" selected>tous les domaines</option>
                        <#list data.domains as domain>
                            <option value="${domain.slug}">${domain.name}</option>
                        </#list>
                    </select>
                </div>
                <button id="linkButton" type="button">
                    <img src="/assets/img/research.png" alt="">
                </button>
            </form>
        </section>

        <section class="offres">
            <h2>Les offres d'emploi du mois</h2>
            <div class="slide">
                <#-- Boucle sur les offres -->
                <#list data.jobs as job>
                    <#if job?counter <= 6>
                        <div class="item">
                            <#-- icône de sauvegarde -->
                            <img src="/assets/img/save.png" class="save" alt="Sauvegarder l'offre" />

                            <div class="heading">
                                <#-- image de l'offre -->
                                <#if job.img??>
                                    <img src="${job.img}" alt="${job.title?default('Offre')}" />
                                <#else>
                                    <img src="/assets/img/offer-1.png" alt="Offre par défaut" />
                                </#if>

                                <div>
                                    <#if job.title??>
                                        <a href="/offre-emploi-maroc/${job.key}" title="${job.title}">
                                            <h2>${job.title}</h2>
                                        </a>
                                    <#else>
                                        <h2>Offre d'emploi</h2>
                                    </#if>
                                    <span>
                                        <img src="/assets/img/business_center.png" alt="Secteur" />
                                        ${job.domain!job.type!''}
                                    </span>
                                    <span>
                                        <img src="/assets/img/place.png" alt="Lieu" />
                                        ${job.city!''}
                                    </span>
                                </div>
                            </div>

                            <span class="badge danger">${job.type!''}</span>
                            <span class="orange">(${job.positionsCount!1} poste(s) ouvert)</span>
                        </div>
                    </#if>
                </#list>
            </div>
        </section>

        <section class="add-job">
            <h2>Vous êtes un <span>employeur</span></h2>
            <p>
                indiquez les compétences nécessaires pour le poste en cliquant sur le
                bouton ci-dessous
            </p>
            <a href="/ajouter-offre-emploi">Ajouter</a>
        </section>

        <section class="offres">
            <h2 class="mb-2">Les offres d'emploi</h2>
            <p>
                Vous êtes à la recherche d'une offre d'emploi au Maroc ? Que vous
                cherchiez un CDI, CDD ou une mission en Intérim, parcourez dans notre
                liste ci-dessous nos annonces d'emploi au Maroc et trouvez celle qui
                correspond à votre profil.
            </p>
            <div class="table-offre">
                <#-- Boucle sur les offres et limite à 6 premières -->
                <#list data.jobs as job>
                    <#if job?counter <= 5>
                        <div class="offre">
                            <#-- Image de l’offre (avec fallback) -->
                            <#if job.img?? && job.img?has_content>
                                <img src="${job.img}" alt="${job.title! 'Offre'}" />
                            <#else>
                                <img src="/assets/img/offer-1.png" alt="Offre par défaut" />
                            </#if>

                            <div>
                                <#-- Titre cliquable vers la fiche -->
                                <#if job.title??>
                                    <a href="/offre-emploi-maroc/${job.key}" title="${job.title}">
                                        <h3>${job.title}</h3>
                                    </a>
                                <#else>
                                    <h3>Offre d'emploi</h3>
                                </#if>

                                <#-- Domaine ou secteur -->
                                <span>
                                    <img src="/assets/img/business_center.png" alt="Secteur" />
                                    ${job.domain!job.type! 'Autre'}
                                </span>

                                <#-- Lieu -->
                                <span>
                                    <img src="/assets/img/place.png" alt="Lieu" />
                                    ${job.city! 'N/C'}
                                </span>
                                <span class="orange">(${job.positionsCount!1} poste(s) ouvert)</span>
                            </div>

                            <div>
                                <#if job.type?? && job.type?has_content>
                                    <span class="badge">${job.type}</span>
                                </#if>
                            </div>
                        </div>
                    </#if>
                </#list>
            </div>
        </section>

        <section class="city" style="background: rgba(236, 237, 242, 0.15)">
            <#-- H2 pour les villes depuis les zones -->
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

            <#-- Texte pour les villes depuis les zones -->
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
            <p>${textVilleContent}</p>

            <div class="box-city">
                <a href="/region/casablanca-settat/casablanca" class="item-city item-1">
                    <img src="assets/img/casablanca.png" alt="" srcset="" />
                    <div class="box-content">
                            <h3>Casablanca</h3>
                    </div>
                </a>
                <a href="/region/tanger-tetouan-al-hoceima/tanger" class="item-city item-2">
                    <img src="assets/img/tanger.png" alt="" srcset="" />
                    <div class="box-content">
                        <h3>Tanger</h3>
                    </div>
                </a>
                <a href="/region/fes-meknes/fes" class="item-city item-3">
                    <img src="assets/img/fes.png" alt="" srcset="" />
                    <div class="box-content">
                        <h3>Fes</h3>
                    </div>
                </a>
                <a href="/region/fes-meknes/meknes" class="item-city item-4">
                    <img src="assets/img/meknes.png" alt="" srcset="" />
                    <div class="box-content">
                        <h3>Meknes</h3>
                    </div>
                </a>
                <a href="/region/rabat-sale-kenitra/rabat" class="item-city item-5">
                    <img src="assets/img/rabat.png" alt="" srcset="" />
                    <div class="box-content">
                        <h3>Rabat</h3>
                    </div>
                </a>
                <a href="/region/souss-massa/agadir" class="item-city item-6">
                    <img src="assets/img/Agadir.jpg" alt="" srcset="" />
                    <div class="box-content">
                        <h3>Agadir</h3>
                    </div>
                </a>
                <a href="/region/dakhla-oued-ed-dahab/dakhla" class="item-city item-8">
                    <img src="assets/img/dakhla.jpg" alt="" srcset="" />
                    <div class="box-content">
                        <h3>Dakhla</h3>
                    </div>
                </a>
                <a href="/region/laayoune-sakia-el-hamra/laayoune" class="item-city item-9">
                    <img src="assets/img/laayoune.jpg" alt="" srcset="" />
                    <div class="box-content">
                        <h3>Laâyoune</h3>
                    </div>
                </a>
            </div>
            <a class="plus" href="/region"
            >Voir plus <img src="assets/img/right-arrow.png" alt=""
            /></a>
        </section>
        <section class="regions">
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
            <h2 class="mb-2">${h2CategorieContent}</h2>
            <#assign textBlockCategorie = "Domaines populaires">
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
            <p class="subtitle">
                ${textBlockCategorie}
            </p>
            <div class="regions-grid">
                <a href="/region/tanger-tetouan-al-hoceima">
                    <div class="region">Tanger-Tétouan-Al Hoceima</div>
                </a>
                <a href="/region/l-oriental">
                    <div class="region">L'Oriental</div>
                </a>
                <a href="/region/fes-meknes">
                    <div class="region">Fès-Meknès</div>
                </a>
                <a href="/region/rabat-sale-kenitra">
                    <div class="region">Rabat-Salé-Kénitra</div>
                </a>
                <a href="/region/beni-mellal-khenifra">
                    <div class="region">Béni Mellal-Khénifra</div>
                </a>
                <a href="/region/casablanca-settat">
                    <div class="region">Casablanca-Settat</div>
                </a>
                <a href="/region/marrakech-safi">
                    <div class="region">Marrakech-Safi</div>
                </a>
                <a href="/region/draa-tafilalet">
                    <div class="region">Drâa-Tafilalet</div>
                </a>
                <a href="/region/souss-massa">
                    <div class="region">Souss-Massa</div>
                </a>
                <a href="/region/guelmim-oued-noun">
                    <div class="region">Guelmim-Oued Noun</div>
                </a>
                <a href="/region/laayoune-sakia-el-hamra">
                    <div class="region">Laâyoune-Sakia El Hamra</div>
                </a>
                <a href="/region/dakhla-oued-ed-dahab">
                    <div class="region">Dakhla-Oued Ed Dahab</div>
                </a>
            </div>
        </section>
        <section class="domaine">
            <#-- H3 pour les catégories depuis les zones -->
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
            <h3>${h2CategorieContent}</h3>

            <div class="table-domain">
                <#-- Affichage des 8 premiers domaines -->
                <#list data.domains as domain>
                    <#if domain?counter <= 8>
                        <div class="col domain-item">
                            <a href="/categorie/${domain.slug}">
                                <i id="large" class="${domain.icon}"></i>
                                <h4>${domain.name}</h4>
                            </a>
                        </div>
                    </#if>
                </#list>

                <#-- Affichage des domaines supplémentaires (cachés initialement) -->
                <#if (data.domains?size > 8)>
                    <#list data.domains as domain>
                        <#if (domain?counter > 8)>
                            <div class="col domain-item domain-hidden" style="display: none;">
                                <a href="/categorie/${domain.slug}">
                                    <i id="large" class="${domain.icon}"></i>
                                    <h4>${domain.name}</h4>
                                </a>
                            </div>
                        </#if>
                    </#list>
                </#if>
            </div>

            <#-- Bouton "Voir plus" affiché seulement s'il y a plus de 8 domaines -->
            <#if (data.domains?size > 8)>
                <a href="#" id="voir-plus-btn" onclick="toggleDomains(event)">Voir plus</a>
                <a href="#" id="voir-moins-btn" onclick="toggleDomains(event)" style="display: none;">Voir moins</a>
            <#else>
                <a href="/categorie">Voir plus</a>
            </#if>
        </section>

        <#include "/common-new/footer.ftl">
        <script src="${assetsPath}/js/script.js" defer></script>
        <script src="${assetsPath}/js/index.js" defer></script>
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
    </body>
</html>