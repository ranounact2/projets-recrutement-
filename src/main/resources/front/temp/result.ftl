<!DOCTYPE html>
<html lang="en">
    <head class="head-section">
        <link rel="stylesheet" href="/assets/css-min/temp/style.min.css" defer/>
        <link rel="stylesheet" href="/assets/css-min/temp/result.min.css" defer/>
    </head>
    <#include "/common/temp/head.ftl">
    <body class="result-page">
        <!-- header -->
        <#include "/common/temp/header.ftl">
        <!-- intro -->

        <section class="intro">
            <h1>Des offres d’emploi au Maroc.</h1>
            <p>Votre nouvel emploi vous attend</p>
            <form action="">
                <div>
                    <svg xmlns="http://www.w3.org/2000/svg" width="27" height="27" viewBox="0 0 27 27" fill="none">
                        <g clip-path="url(#clip0_1_726)">
                            <path
                            d="M22.5 5.625H4.5C3.2625 5.625 2.26125 6.6375 2.26125 7.875L2.25 19.125C2.25 20.3625 3.2625 21.375 4.5 21.375H22.5C23.7375 21.375 24.75 20.3625 24.75 19.125V7.875C24.75 6.6375 23.7375 5.625 22.5 5.625ZM12.375 9H14.625V11.25H12.375V9ZM12.375 12.375H14.625V14.625H12.375V12.375ZM9 9H11.25V11.25H9V9ZM9 12.375H11.25V14.625H9V12.375ZM7.875 14.625H5.625V12.375H7.875V14.625ZM7.875 11.25H5.625V9H7.875V11.25ZM18 19.125H9V16.875H18V19.125ZM18 14.625H15.75V12.375H18V14.625ZM18 11.25H15.75V9H18V11.25ZM21.375 14.625H19.125V12.375H21.375V14.625ZM21.375 11.25H19.125V9H21.375V11.25Z"
                            fill="#696969" />
                        </g>
                        <defs>
                            <clipPath id="clip0_1_726">
                                <rect width="27" height="27" fill="white" />
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
                            fill="#696969" />
                        </g>
                        <defs>
                            <clipPath id="clip0_1_710">
                                <rect width="27" height="27" fill="white" />
                            </clipPath>
                        </defs>
                    </svg>
                    <select name="" id="">
                        <option value="" disabled selected>toutes les villes</option>
                        <option></option>
                    </select>
                </div>
                <div>
                    <svg xmlns="http://www.w3.org/2000/svg" width="27" height="27" viewBox="0 0 27 27" fill="none">
                        <g clip-path="url(#clip0_1_723)">
                            <path
                            d="M11.25 18V16.875H3.38625L3.375 21.375C3.375 22.6238 4.37625 23.625 5.625 23.625H21.375C22.6238 23.625 23.625 22.6238 23.625 21.375V16.875H15.75V18H11.25ZM22.5 7.875H17.9888V5.625L15.7388 3.375H11.2388L8.98875 5.625V7.875H4.5C3.2625 7.875 2.25 8.8875 2.25 10.125V13.5C2.25 14.7487 3.25125 15.75 4.5 15.75H11.25V13.5H15.75V15.75H22.5C23.7375 15.75 24.75 14.7375 24.75 13.5V10.125C24.75 8.8875 23.7375 7.875 22.5 7.875ZM15.75 7.875H11.25V5.625H15.75V7.875Z"
                            fill="#696969" />
                        </g>
                        <defs>
                            <clipPath id="clip0_1_723">
                                <rect width="27" height="27" fill="white" />
                            </clipPath>
                        </defs>
                    </svg>
                    <select name="" id="">
                        <option value="" disabled selected>tous les domaines</option>
                        <option></option>
                    </select>
                </div>
                <button><a href="./result.html"><img src="../assets/img/research.png" alt=""></a></button>
            </form>
        </section>

        <!-- breadcrumb  -->
        <div class="bread">
            <div class="titre m-1">
                <h2>Résultat</h2>
                <div class="breadcrumbs" id="breadcrumbs">

                    <a class="breadcrumbs__link" href="#">Accueil</a>
                    <span> > </span>
                    <br>

                    <a class="breadcrumbs__link" href="#">Apropos</a>
                    <span> > </span>
                    <br>

                    <span>Ajouter une offre d'emploi</span>
                </div>
            </div>
        </div>

        <!-- les offres -->
        <#if data?? && data.jobs??>
            <section class="offres">
                <h2 class="mb-2">Les offres d'emploi</h2>
                <p>
                    Vous êtes à la recherche d’une offre d’emploi au Maroc ? Que vous cherchiez un CDI, CDD ou une mission en Intérim,
                    parcourez dans notre liste ci-dessous nos annonces d’emploi au Maroc et trouvez celle qui correspond à votre profil.
                </p>
                <div class="table-offre">
                    <#list data.jobs as job>
                        <div class="offre">
                            <#-- image de l’offre -->
                            <#if job.img??>
                                <img src="/assets/img/${job.img}" alt="${job.title?default('Offre')}">
                            <#else>
                                <img src="/assets/img/offer-1.png" alt="Offre par défaut">
                            </#if>

                            <div>
                                <h3>
                                    <#if job.title??>
                                        <a href="/offre-emploi-maroc/${job.key}"
                                        title="${job.title}">${job.title}</a>
                                    <#else>
                                        Offre d'emploi
                                    </#if>
                                </h3>
                                <span><img src="/assets/img/place.png" alt="Lieu">${job.city!''}</span>
                                <span><img src="/assets/img/business_center.png" alt="Secteur">
                                    ${job.category!job.type!''}
                                </span>
                                <span class="orange">(${job.positionsCount!1} poste(s) ouvert)</span>
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
                </div>

                <#--                    <ul class="pagination">-->
                <#--                        <li><a href="?page=${data.pageNumber - 1}&size=${data.pageSize}">‹</a></li>-->
                <#--                        <li><a href="?page=1&size=${data.pageSize}">1</a></li>-->
                <#--                        <li><a href="?page=2&size=${data.pageSize}">2</a></li>-->
                <#--                        <li><img src="/assets/img/arrow.png" alt="Next"></li>-->
                <#--                    </ul>-->
            </section>
        </#if>
        <!-- Villes -->

        <section class="city">
            <div class="container">
                <div class="site-heading">
                    <h2>Les <span>villes</span> qui recrutent</h2>
                    <br>
                    <p style="font-weight: bolder;">Découvrez les offres d’emploi dans les villes avec le plus grand nombre d’opportunités d’emploi.</p>
                </div>

                <div class="cities">
                    <div class="row">
                        <div class="column">
                            <div class="city-list">
                                <ul>
                                    <li>
                                        <p class="title">Offre en Informatique</p>
                                        <a href="/ville/casablanca">Casablanca</a>
                                    </li>
                                    <li>
                                        <p class="title">Offre en Informatique</p>
                                        <a href="/ville/tanger">Tanger</a>
                                    </li>
                                    <li>
                                        <p class="title">Offre en Informatique</p>
                                        <a href="/ville/rabat">Rabat</a>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div class="column">
                            <div class="city-list">
                                <ul>
                                    <li>
                                        <p class="title">Offre en Informatique</p>
                                        <a href="/ville/mohammedia">Mohammedia</a>
                                    </li>
                                    <li>
                                        <p class="title">Offre en Informatique</p>
                                        <a href="/ville/kenitra">Kénitra</a>
                                    </li>
                                    <li>
                                        <p class="title">Offre en Informatique</p>
                                        <a href="/ville/berrechid">Berrechid</a>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div class="column">
                            <div class="city-list">
                                <ul>
                                    <li>
                                        <p class="title">Offre en Informatique</p>
                                        <a href="/ville/fes">Fes</a>
                                    </li>
                                    <li>
                                        <p class="title">Offre en Informatique</p>
                                        <a href="/ville/marrakech">Marrakech</a>
                                    </li>
                                    <li>
                                        <p class="title">Offre en Informatique</p>
                                        <a href="/ville/taza">Taza</a>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div class="column">
                            <div class="city-list">
                                <ul>
                                    <li>
                                        <p class="title">Offre en Informatique</p>
                                        <a href="/ville/meknes">Meknes</a>
                                    </li>
                                    <li>
                                        <p class="title">Offre en Informatique</p>
                                        <a href="/ville/tetouan">Tetouan</a>
                                    </li>
                                    <li>
                                        <p class="title">Offre en Informatique</p>
                                        <a href="/ville/bouznika">Bouznika</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="column">
                            <div class="city-list">
                                <ul>
                                    <li>
                                        <p class="title">Offre en Informatique</p>
                                        <a href="/ville/Agadir">Agadir</a>
                                    </li>
                                    <li>
                                        <p class="title">Offre en Informatique</p>
                                        <a href="/ville/alhoceima">Al hoceima</a>
                                    </li>
                                    <li>
                                        <p class="title">Offre en Informatique</p>
                                        <a href="/ville/aoussered">Aoussered</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="column">
                            <div class="city-list">
                                <ul>
                                    <li><a href="/ville/assilah">Assilah</a></li>
                                    <li><a href="/ville/azrou">Azrou</a></li>
                                    <li><a href="/ville/benimellal">Beni mellal</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="column">
                            <div class="city-list">
                                <ul>
                                    <li><a href="/ville/benslimane">Benslimane</a></li>
                                    <li><a href="/ville/berkane">Berkane</a></li>
                                    <li><a href="/ville/berrechid">Berrechid</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="column">
                            <div class="city-list">
                                <ul>
                                    <li><a href="/ville/boujdour">Boujdour</a></li>
                                    <li><a href="/ville/bouskoura">Bouskoura</a></li>
                                    <li><a href="/ville/bouznika">Bouznika</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="column">
                            <div class="city-list">
                                <ul>
                                    <li><a href="/ville/casablanca">Casablanca</a></li>
                                    <li><a href="/ville/chafchaouen">Chafchaouen</a></li>
                                    <li><a href="/ville/dakhla">Dakhla</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="column">
                            <div class="city-list">
                                <ul>
                                    <li><a href="/ville/eljadida">El jadida</a></li>
                                    <li><a href="/ville/errachidia">Errachidia</a></li>
                                    <li><a href="/ville/essaouira">Essaouira</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="column">
                            <div class="city-list">
                                <ul>
                                    <li><a href="/ville/essmara">Essmara</a></li>
                                    <li><a href="/ville/fes">Fés</a></li>
                                    <li><a href="/ville/fik-ben-salah">Fkih ben salah</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="column">
                            <div class="city-list">
                                <ul>
                                    <li><a href="/ville/guelmim">Guelmim</a></li>
                                    <li><a href="/ville/guercif">Guercif</a></li>
                                    <li><a href="/ville/ifrane">Ifrane</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="column">
                            <div class="city-list">
                                <ul>
                                    <li><a href="/ville/kenitra">Kénitra</a></li>
                                    <li><a href="/ville/kabila">Kabila</a></li>
                                    <li><a href="/ville/khenifra">Khenifra</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="column">
                            <div class="city-list">
                                <ul>
                                    <li><a href="/ville/khmisset">Khmisset</a></li>
                                    <li><a href="/ville/khouribga">Khouribga</a></li>
                                    <li><a href="/ville/ksar-el-kbir">Ksar el kbir</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="column">
                            <div class="city-list">
                                <ul>
                                    <li><a href="/ville/laayoune">Laayoune</a></li>
                                    <li><a href="/ville/larache">Larache</a></li>
                                    <li><a href="/ville/marrakech">Marrakech</a></li>
                                </ul>
                            </div>
                        </div>

                    </div>

                </div>
            </div>

            <button class="show-more ds" onclick="toggleCities()">Voir plus</button>

        </section>
        <!-- Regions -->
        <section class="regions">
            <h2 class="mb-2">Les régions qui recrutent</h2>
            <p class="subtitle">
                Explorez les régions du Maroc où les opportunités d’emploi sont les plus nombreuses.
            </p>
            <div class="regions-grid">
                <div class="region">Tanger-Tétouan-Al Hoceima</div>
                <div class="region">L'Oriental</div>
                <div class="region">Fès-Meknès</div>
                <div class="region">Rabat-Salé-Kénitra</div>
                <div class="region">Béni Mellal-Khénifra</div>
                <div class="region">Casablanca-Settat</div>
                <div class="region">Marrakech-Safi</div>
                <div class="region">Drâa-Tafilalet</div>
                <div class="region">Souss-Massa</div>
                <div class="region">Guelmim-Oued Noun</div>
                <div class="region">Laâyoune-Sakia El Hamra</div>
                <div class="region">Dakhla-Oued Ed Dahab</div>
            </div>
        </section>



        <!-- Domains -->

        <section class="domaine">
            <div class="site-heading">
                <h3><span>Domaines</span> populaires</h3>
                <br>
                <p style="font-weight: bolder; text-align: center;">Trouvez sur la liste ci-dessous les offres d’emploi qui correspondent à votre domaine de spécialité.</p>
            </div>
            <div class="table-domain">
                <div class="col">
                    <i id="large" class="fas fa-laptop"></i>
                    <a>Informatique</a>
                    <p>400 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-bullhorn"></i>
                    <a>Marketing</a>
                    <p>100 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-shopping-bag"></i>
                    <a>Commerce</a>
                    <p>85 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-wifi"></i>
                    <a>Télécommunication</a>
                    <p>120 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-wallet"></i>
                    <a>Banque/finance</a>
                    <p>40 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-industry"></i>
                    <a>Industrie</a>
                    <p>200 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-user-shield"></i>
                    <h4>Assurance</h4>
                    <p>60 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-calculator" ></i>
                    <a>Comptabilité/Audit</a>
                    <p>50 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-comments"></i>
                    <a>Publication/Impression</a>
                    <p>50 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-sign"></i>
                    <a>Immobilier</a>
                    <p>50 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-suitcase-rolling"></i>
                    <a>Tourisme-hotellerie-et-restauration</a>
                    <p>50 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-shipping-fast"></i>
                    <a>Transport-logistique</a>
                    <p>50 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-running"></i>
                    <a>Sport-divertissement</a>
                    <p>50 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-users"></i>
                    <a>Ressources humaines</a>
                    <p>50 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-people-arrows"></i>
                    <a>Relations Publiques</a>
                    <p>50 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-user-edit"></i>
                    <a>Secrétariat</a>
                    <p>50 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-tractor"></i>
                    <a>Agriculture</a>
                    <p>50 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-sitemap"></i>
                    <a>Architecture</a>
                    <p>50 postes ouverts</p>
                </div>
                <div class="col">
                    <i id="large" class="fas fa-wallet"></i>
                    <a>Banque/finance</a>
                    <p>50 postes ouverts</p>
                </div>

            </div>
            <a class="kh" href="http://">Voir plus</a>
        </section>

        <!-- footer -->

        <#include "/common/footer.ftl">
        <script src="assets/js/temp/script.js" defer></script>
    </body>
</html>