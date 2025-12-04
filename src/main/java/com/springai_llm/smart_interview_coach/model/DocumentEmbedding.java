package com.springai_llm.smart_interview_coach.model;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Map;

@Entity
@Table(name = "interview_chunks")
@Setter
@Getter
public class DocumentEmbedding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chunk_text", columnDefinition = "TEXT")
    private String chunkText;

    @Column(name = "embedding", columnDefinition = "vector(1536)")
    private String embedding;

    @Column(name = "metadata", columnDefinition = "jsonb")
    @Type(JsonType.class)
    private Map<String, Object> metadata;
}

