package com.centoria.jobmaroc.service.impl;

import com.centoria.jobmaroc.service.IAiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AiService implements IAiService {

    private static AiService instance = null;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String NVIDIA_API_KEY;

    private AiService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
        
        // Load API key from environment variable or fallback for local testing
        String apiKey = System.getenv("NVIDIA_API_KEY");
        this.NVIDIA_API_KEY = apiKey != null ? apiKey : "YOUR_NVIDIA_API_KEY";
    }

    public static IAiService getInstance() {
        if (instance == null) {
            instance = new AiService();
        }
        return instance;
    }

    @Override
    public List<Double> generateEmbedding(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            // We use snowflake/arctic-embed-l via NVIDIA NIM which provides excellent 1024-dimension embeddings
            String requestBody = objectMapper.writeValueAsString(
                java.util.Map.of(
                    "input", List.of(text),
                    "model", "snowflake/arctic-embed-l",
                    "input_type", "query",
                    "encoding_format", "float"
                )
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://integrate.api.nvidia.com/v1/embeddings"))
                    .header("Authorization", "Bearer " + NVIDIA_API_KEY)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                JsonNode embeddingArray = root.path("data").get(0).path("embedding");
                
                List<Double> embedding = new ArrayList<>();
                if (embeddingArray.isArray()) {
                    for (JsonNode val : embeddingArray) {
                        embedding.add(val.asDouble());
                    }
                }
                return embedding;
            } else {
                log.error("NVIDIA API Error: {} - {}", response.statusCode(), response.body());
            }
        } catch (Exception e) {
            log.error("Failed to generate embedding with NVIDIA NIM", e);
            throw new RuntimeException("Embedding generation failed", e);
        }
        
        throw new RuntimeException("Embedding API did not return a valid response");
    }
}
