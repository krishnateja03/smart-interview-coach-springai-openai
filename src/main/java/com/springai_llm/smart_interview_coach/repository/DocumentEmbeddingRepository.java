package com.springai_llm.smart_interview_coach.repository;

import com.springai_llm.smart_interview_coach.model.DocumentEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentEmbeddingRepository extends JpaRepository<DocumentEmbedding, Long> {
    @Query(value = "SELECT * \n" +
            "            FROM interview_chunks\n" +
            "            ORDER BY embedding <-> CAST(:embedding AS vector)\n" +
            "            LIMIT :topK",
            nativeQuery = true)
    List<DocumentEmbedding> searchBySimilarity(
            @Param("embedding") float[] embedding,
            @Param("topK") int topK
    );
}
