<!DOCTYPE html>
<html lang="en">
    <body>
        <div class="cookies-banner" id="cookies-banner">
            En poursuivant votre navigation, vous acceptez l’utilisation de cookies pour une meilleure expérience utilisateur.
            <button id="more-cookies-info-button" class="cookies-banner__know-more">
                En savoir plus
            </button>
            <button class="cookies-banner__accept" id="accept-cookies-button">OK</button>
        </div>

<#-- Valeurs par défaut pour le footer -->
<#assign footerTitleDefault = "Les villes principales">
<#assign footerTextDefault = "Des offres d'emploi au Maroc">
<#assign footerBottomDefault = "">

<#-- Surcharge par les zones dynamiques (CORRIGÉ : utilisation de zones au lieu de zp) -->
<#if zones??>
    <#list zones as zonePage>
        <#if zonePage.zones??>
            <#list zonePage.zones?keys as zoneKey>
                <#assign currentZone = zonePage.zones[zoneKey]>
                <#if currentZone.values??>
                    <#if currentZone.values.title_footer??>
                        <#assign footerTitleDefault = currentZone.values.title_footer.content!currentZone.values.title_footer.value!''>
                    </#if>
                    <#if currentZone.values.text_footer??>
                        <#assign footerTextDefault = currentZone.values.text_footer.content!currentZone.values.text_footer.value!''>
                    </#if>
                    <#if currentZone.values.text_footer_bottom??>
                        <#assign footerBottomDefault = currentZone.values.text_footer_bottom.content!currentZone.values.text_footer_bottom.value!''>
                    </#if>
                </#if>
            </#list>
        </#if>
    </#list>
</#if>

        <footer class="footer-section">
            <div class="footer-container">
                <div class="footer-left">
                    <a href="/">
                        <img src="/assets/img/logo.png" alt="logo" class="logo" />
                    </a>
                    <h3 class="footer-head">${footerTitleDefault}</h3>
                    <p class="footer-text">${footerTextDefault}</p>
                    <div class="social-icons">
                        <img
                        src="/assets/img/facebook-logo.svg"
                        alt="facebook logo"
                        class="facebook-logo"
                        />
                        <img
                        src="/assets/img/linkedin-logo.svg"
                        alt="linkedin logo"
                        class="linkedin-logo"
                        />
                    </div>
                </div>

                <div class="footer-right">
                    <div>
                        <input type="checkbox" id="villes-toggle" class="footer-toggle" />
                        <label for="villes-toggle" class="footer-label">
                            <span class="toggle-icon"></span>

                            <h4>Les villes principales</h4>
                        </label>
                        <ul class="footer-list">
                            <li class="menu-item">
                                <a href="/region/tanger-tetouan-al-hoceima/tanger">Les offres d'emploi à Tanger</a>
                            </li>
                            <li class="menu-item">
                                <a href="/region/casablanca-settat/casablanca">Les offres d'emploi à Casablanca</a>
                            </li>
                            <li class="menu-item">
                                <a href="/region/rabat-sale-kenitra/rabat">Les offres d'emploi à Rabat</a>
                            </li>
                            <li class="menu-item">
                                <a href="/region/souss-massa/agadir">Les offres d'emploi à Agadir</a>
                            </li>
                        </ul>
                    </div>

                    <div>
                        <input type="checkbox" id="domaines-toggle" class="footer-toggle" />
                        <label for="domaines-toggle" class="footer-label">
                            <span class="toggle-icon"></span>

                            <h4>Les offres par domaine</h4>
                        </label>
                        <ul class="footer-list">
                            <li class="menu-item">
                                <a href="/categorie/informatique">Informatique</a>
                            </li>
                            <li class="menu-item">
                                <a href="/categorie/marketing">Marketing</a>
                            </li>
                            <li class="menu-item">
                                <a href="/categorie/telecommunication">Télécommunication</a>
                            </li>
                            <li class="menu-item">
                                <a href="/categorie/comptabilite-audit">Comptabilité/Audit</a>
                            </li>
                        </ul>
                    </div>

                    <div>
                        <input type="checkbox" id="liens-toggle" class="footer-toggle" />
                        <label for="liens-toggle" class="footer-label">
                            <span class="toggle-icon"></span>

                            <h4>Liens principaux</h4>
                        </label>
                        <ul class="footer-list">
                            <li class="menu-item"><a href="/">Accueil</a></li>
                            <li class="menu-item">
                                <a href="/a-propos-emplois-maroc">A propos</a>
                            </li>
                            <li class="menu-item">
                                <a href="/mes-annonces-emploi">Mes offres</a>
                            </li>
                            <li class="menu-item"><a href="/contact">Contact</a></li>
                            <li class="menu-item"><a href="/mentions">Mentions légales</a></li>
                        </ul>
                    </div>
                </div>
            </div>
    <#if footerBottomDefault?has_content>
        <p class="footer-bottom">${footerBottomDefault}</p>
    <#else>
            <p class="footer-bottom"></p>
    </#if>
        </footer>
    </body>
    <!-- Footer End -->
</html>