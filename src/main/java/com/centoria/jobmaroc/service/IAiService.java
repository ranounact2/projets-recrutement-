package com.centoria.jobmaroc.service;

import java.util.List;

public interface IAiService {
    /**
     * Generates a vector embedding for the given text.
     * @param text The text to encode
     * @return A list of doubles representing the vector embedding
     */
    List<Double> generateEmbedding(String text);
}
