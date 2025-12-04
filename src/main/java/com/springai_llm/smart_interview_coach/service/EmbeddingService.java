package com.springai_llm.smart_interview_coach.service;

import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.stereotype.Service;

@Service
public class EmbeddingService {
    private final OpenAiEmbeddingModel embeddingModel;

    public EmbeddingService(OpenAiEmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public float[] embed(String text) {
        float[] response = embeddingModel.embed(text);
        return response;
    }
}
