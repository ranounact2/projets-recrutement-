package com.centoria.jobmaroc.service.impl;

import com.centoria.jobmaroc.common.context.ApplicationContext;
import com.centoria.jobmaroc.service.IStorageService;
import jodd.props.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URI;

/**
 * Implémentation du service S3 pour OVH Object Storage.
 * Compatible avec l'API AWS S3 (signature v4).
 */
public class S3StorageService implements IStorageService {

    private static final Logger logger = LoggerFactory.getLogger(S3StorageService.class);

    private static IStorageService instance = null;

    private final boolean enabled;
    private final String bucket;
    private final String endpoint;
    private final S3Client s3Client;

    private S3StorageService() {
        Props props = ApplicationContext.getInstance().getProps();

        String enabledStr = props.getValue("s3.enabled");
        this.enabled = "true".equalsIgnoreCase(enabledStr);

        this.endpoint = props.getValue("s3.endpoint");
        this.bucket   = props.getValue("s3.bucket");
        String region    = props.getValue("s3.region");
        String accessKey = props.getValue("s3.access.key");
        String secretKey = props.getValue("s3.secret.key");

        if (this.enabled) {
            if (isEmpty(endpoint) || isEmpty(bucket) || isEmpty(region)
                    || isEmpty(accessKey) || isEmpty(secretKey)) {
                logger.error("Configuration S3 incomplète — upload désactivé malgré s3.enabled=true");
                this.s3Client = null;
            } else {
                this.s3Client = S3Client.builder()
                        .endpointOverride(URI.create(endpoint))
                        .region(Region.of(region))
                        .credentialsProvider(
                                StaticCredentialsProvider.create(
                                        AwsBasicCredentials.create(accessKey, secretKey)))
                        .httpClient(UrlConnectionHttpClient.create())
                        .forcePathStyle(true)   // obligatoire pour OVH : endpoint/bucket/key
                        .build();
                logger.info("Client S3 initialisé — endpoint: {}, bucket: {}", endpoint, bucket);
            }
        } else {
            this.s3Client = null;
            logger.info("Upload S3 désactivé (s3.enabled=false)");
        }
    }

    public static IStorageService getInstance() {
        if (instance == null) {
            instance = new S3StorageService();
        }
        return instance;
    }

    @Override
    public boolean isAvailable() {
        return enabled && s3Client != null;
    }

    @Override
    public String store(String key, byte[] data, String contentType) {
        if (!isAvailable()) {
            logger.debug("Stockage S3 ignoré (désactivé): {}", key);
            return null;
        }

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(contentType)
                    .contentLength((long) data.length)
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(data));
            logger.info("Fichier stocké sur OVH S3 — clé: {}", key);
            return key;
        } catch (Exception e) {
            logger.error("Erreur lors du stockage S3 — clé: {}", key, e);
            return null;
        }
    }

    @Override
    public String getUrl(String key) {
        if (isEmpty(endpoint) || isEmpty(bucket) || isEmpty(key)) return null;
        return endpoint + "/" + bucket + "/" + key;
    }

    private static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
