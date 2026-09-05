package com.centoria.jobmaroc.service;

import com.centoria.jobmaroc.dto.AdDto;
import com.centoria.jobmaroc.dto.MailDto;
import com.centoria.jobmaroc.model.Ad;

import java.util.List;

public interface IAdService extends IBaseService<Ad> {
    void renderTemplate(MailDto<AdDto> mdto, String emailTemplate);

    void initStateAndType(Ad ad, String state, int stateRank);

    void updateState(Ad ad); // update full object


    List<AdDto> getAddsByDomain(String domain, int pageNumber, int numOfJobs, String keyword);

    List<AdDto> getAdsByDomainSlug(String domainSlug, int pageNumber, int numOfAds);

    List<Ad> getAdsByCitySlug(String citySlug, int numberOfPage, int numOfAds, String keyword);

    List<AdDto> getAllAds(int pageNumber, int numberOfAds);

    /**
     * Liste paginée pour le back-office, avec filtre optionnel sur le statut MongoDB {@code state}.
     *
     * @param boStatusFilter {@code null} ou {@code "all"} : toutes les annonces ;
     *                        {@code "new"} : {@code new} et {@code new_verified} ;
     *                        {@code "modified"} : {@code updated} et {@code updated_verified}.
     */
    List<AdDto> getAllAds(int pageNumber, int numberOfAds, String boStatusFilter);

    /**
     * Compte les annonces pour le back-office (même filtre que {@link #getAllAds(int, int, String)}).
     */
    long countForBoListing(String boStatusFilter);

    List<AdDto> getAdsWithDomainAndCity(String citySlug, String domainSlug, int pageNumber, int numberOfAds, String keyword);

    long countAdsWithDomainAndCity(String citySlug, String domainSlug, String keyword);

    List<AdDto> getAdsByDefaults(int pageNumber, int numberOfAds);

    List<Ad> getAdsByCity(String city, int pageNumber, int numberOfAds, String keyword);

    List<AdDto> getSimilarAds(String code, String domain);

    List<AdDto> getAdsByKeyword(String keyword, int pageNumber, int numberOfAds);

    List<AdDto> getAdsWithDomainSlugAndCitySlug(String citySlug, String domainSlug, int pageNumber, int numberOfAds);

    List<AdDto> getAdsBySecretCode(String secretCode, int pageNumber, int numberOfAds);

    /**
     * Récupère les annonces par secretCode en gérant les collisions MD5.
     * Si plusieurs emails partagent le même secretCode, retourne uniquement les annonces
     * du groupe d'email le plus récent. Le tri et la pagination sont effectués en base de données.
     * 
     * @param secretCode Le code secret MD5 de l'email
     * @param pageNumber Numéro de page (commence à 1)
     * @param numberOfAds Nombre d'annonces par page
     * @return Liste des annonces triées par date décroissante
     */
    List<AdDto> getAdsBySecretCodeGroupedByEmail(String secretCode, int pageNumber, int numberOfAds);
    
    /**
     * Compte le nombre total d'annonces pour un secretCode après regroupement par email.
     * 
     * @param secretCode Le code secret MD5 de l'email
     * @return Le nombre total d'annonces du groupe d'email le plus récent
     */
    long countAdsBySecretCodeGroupedByEmail(String secretCode);

    List<AdDto> getAdsByRegion(String domainSlug, String region, int pageNumber, int numberOfAds);

    List<Ad> getAdsByRegions(String region, int pageNumber, int numberOfAds);

    List<Ad> getAdsByRegionsAndDomain(String region, int pageNumber, int numberOfAds, String domainSlug);

    List<Ad> getAllAds();

    /**
     * Effectue une recherche sémantique via MongoDB Vector Search
     * @param query Texte à rechercher (ex: CV ou requête naturelle)
     * @param pageNumber Numéro de page
     * @param numberOfAds Nombre de résultats
     * @return Liste d'annonces avec score de pertinence
     */
    List<AdDto> getSemanticAdsByQuery(String query, int pageNumber, int numberOfAds);

}
