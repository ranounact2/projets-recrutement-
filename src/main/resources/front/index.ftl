<!DOCTYPE html>
<html lang = "en">
    <head>

        <link rel="stylesheet" href="/assets/css-min/index.min.css" defer/>
    </head>
    <#include "/common/head.ftl">
    <body>
        <#include "/common/header.ftl">
        <section class="intro">
            <#-- Titre H1 dynamique depuis la zone content, avec fallback -->
            <#assign h1Default = "Des offres d'emploi au Maroc.">
            <#assign pDefault = "Votre nouvel emploi vous attend">
            <#-- Surcharge par les zones dynamiques -->
            <#if zones??>
                <#list zones as zonePage>
                    <#if zonePage.zones??>
                        <#list zonePage.zones?keys as zoneKey>
                            <#assign currentZone = zonePage.zones[zoneKey]>
                            <#if currentZone.values??>
                                <#if currentZone.values.h1??>
                                    <#assign h1Default = currentZone.values.h1.content!currentZone.values.h1.value!''>
                                </#if>
                                <#if currentZone.values.h1_block??>
                                    <#assign pDefault = currentZone.values.h1_block.content!currentZone.values.h1_block.value!''>
                                </#if>
                            </#if>
                        </#list>
                    </#if>
                </#list>
            </#if>
            <h1>${h1Default}</h1>
            <p>${pDefault}</p>
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
                        <option value="" disabled selected>toutes les villes</option>
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
                        <option value="" disabled selected>tous les domaines</option>
                        <#list data.domains as domain>
                            <option value="${domain.slug}">${domain.name}</option>
                        </#list>
                    </select>
                </div>
                <button id="linkButton" type="button" onclick="updateButtonLink()"><img src="assets/img/research.png" alt="">
                </button>
            </form>
        </section>
        <section class="offres">
            <h2 class="mb-2">Les offres d'emploi</h2>
            <p>
                Vous êtes à la recherche d’une offre d’emploi au Maroc ? Que vous cherchiez un CDI, CDD ou une mission en
                Intérim,
                parcourez dans notre liste ci-dessous nos annonces d’emploi au Maroc et trouvez celle qui correspond à votre
                profil.
            </p>
            <div class="table-offre">
                <div class="offre">
                    <img src="assets/img/offer1.png" alt="">
                    <div>
                        <h3>Assistante dentaire qualifiée</h3>
                        <span><img src="assets/img/place.png" alt="">rabat</span>
                        <span><img src="assets/img/business_center.png" alt="">autre</span>
                        <span class="orange">(1 poste ouvert)</span>
                    </div>
                    <div>
                        <span class="badge">CDI - Temps complet</span>
                        <b>Il y a 1 an(s)</b>
                    </div>
                </div>
                <div class="offre">
                    <img src="assets/img/offer2.png" alt="">
                    <div>
                        <h3>stage renumere au centre de demande de visa d’Espagne</h3>
                        <span><img src="assets/img/place.png" alt="">tanger</span>
                        <span><img src="assets/img/business_center.png" alt="">autre</span>
                        <span class="orange">(2 poste ouvert)</span>
                    </div>
                    <div>
                        <span class="badge danger">stage</span>
                        <b>Il y a 1 an(s)</b>
                    </div>
                </div>
                <div class="offre">
                    <img src="assets/img/offer3.png" alt="">
                    <div>
                        <h3>Assistante analyste financier</h3>
                        <span><img src="assets/img/place.png" alt="">sale</span>
                        <span><img src="assets/img/business_center.png" alt="">banque-finance</span>
                        <span class="orange">(2 poste ouvert)</span>
                    </div>
                    <div>
                        <span class="badge gray">A définir</span>
                        <b>Il y a 1 an(s)</b>
                    </div>
                </div>
            </div>
            <ul>
                <li>1</li>
                <li>2</li>
                <li>3</li>
                <li><img src="assets/img/arrow.png" alt=""></li>
            </ul>
        </section>
        <section class="links">
            <h2>Les régions</h2>
            <p>Dans quelle région trouver les offres d'emploi au Maroc ?</p>
            <div class="back-color  row-gap row-gap-block">
                <#list regions as region>
                    <div class="col-3-flex">
                        <a class="list-link" href="region/${region.slug}/">
                            ${region.name}
                        </a>
                    </div>
                </#list>
            </div>

        </div>
    </section>
    <section class="links">
        <h2>Les villes qui recrutent</h2>
        <p>Découvrez les offres d’emploi dans les villes avec le plus grand nombre d’opportunités d’emploi.</p>
        <div class="box-city">
            <div class="item-city item-1">
                <img src="assets/img/casablanca.png" alt="" srcset="">
                <div class="box-content">
                    <h3>Casablanca</h3>
                    <p>3,950,000</p>
                </div>
            </div>
            <div class="item-city item-2">
                <img src="assets/img/tanger.png" alt="" srcset="">
                <div class="box-content">
                    <h3>Tanger</h3>
                    <p>1,349,000</p>
                </div>
            </div>
            <div class="item-city item-3">
                <img src="assets/img/fes.png" alt="" srcset="">
                <div class="box-content">
                    <h3>Fes</h3>
                    <p>1,313,311</p>
                </div>
            </div>
            <div class="item-city item-4">
                <img src="assets/img/meknes.png" alt="" srcset="">
                <div class="box-content">
                    <h3>Meknes</h3>
                    <p>585,841</p>
                </div>
            </div>
            <div class="item-city item-5">
                <img src="assets/img/rabat.png" alt="" srcset="">
                <div class="box-content">
                    <h3>Rabat</h3>
                    <p> 1,989,197</p>
                </div>
            </div>
        </div>
    </section>
    <section class="domaine">
        <h3>Domaines populaires</h3>
        <div class="table-domain">
            <#list data.domains as domain>
                <#if domain?counter <= 8>
                    <div class="col">
                        <a href="categorie/${domain.slug}/">
                            <h4>${domain.name}</h4></a>
                    </div>
                </#if>
            </#list>
        </div>
        <a href="categorie">Voir plus</a>
    </section>
    <#include "/common/footer.ftl">
    <script src="assets/js-min/script.min.js" defer></script>
    <script src="assets/js-min/index.min.js" defer></script>
</body>
</html>
