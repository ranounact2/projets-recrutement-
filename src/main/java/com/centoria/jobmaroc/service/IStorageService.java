package com.centoria.jobmaroc.service;

/**
 * Service de stockage de fichiers.
 * Agnostique du backend utilisé (S3, Azure Blob, disque local, etc.).
 */
public interface IStorageService {

    /**
     * Stocke un fichier et retourne sa clé de stockage.
     *
     * @param key         identifiant/chemin unique du fichier (ex: "candidatures/jobId/nom-prenom-uuid.pdf")
     * @param data        contenu binaire du fichier
     * @param contentType type MIME (ex: "application/pdf")
     * @return la clé du fichier stocké, null en cas d'échec ou si le service est désactivé
     */
    String store(String key, byte[] data, String contentType);

    /**
     * Retourne l'URL d'accès à un fichier à partir de sa clé de stockage.
     *
     * @param key clé retournée par {@link #store}
     * @return URL complète du fichier, null si paramètre manquant
     */
    String getUrl(String key);

    /**
     * Indique si ce service de stockage est disponible et configuré.
     */
    boolean isAvailable();
}
