package com.springai_llm.smart_interview_coach.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RagChunkDto {
    private Long id;
    private String chunkText;

    public RagChunkDto(Long id, String chunkText) {
        this.id = id;
        this.chunkText = chunkText;
    }
}
