package com.springai_llm.smart_interview_coach.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springai_llm.smart_interview_coach.repository.DocumentEmbeddingRepository;
import com.springai_llm.smart_interview_coach.util.TextSplitter;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.springai_llm.smart_interview_coach.util.PGVectorUtils.toPgVectorLiteral;

@Service
@RequiredArgsConstructor
public class VectorStoreService {

    private final EmbeddingService embeddingService;
    private final JdbcTemplate jdbc;

    @Transactional
    public void storePdfKnowledgeBase(String pdfText) {

        List<String> chunks = TextSplitter.splitIntoChunks(pdfText, 500);

        for (String chunk : chunks) {

            float[] embedding = embeddingService.embed(chunk);

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("source","rag_test_interview_knowledge_base.pdf");
            metadata.put("chunk_index", 12);
            metadata.put("timestamp", System.currentTimeMillis());

            try {

//                em.createNativeQuery("INSERT INTO interview_chunks (chunk_text, embedding, metadata) VALUES (:chunk_text, :embedding, :metadata)")
//                        .setParameter("chunk_text", chunk)
//                        .setParameter("embedding", vectorParam)
//                        .setParameter("metadata", metadata)
//                        .executeUpdate();
//                Session session = em.unwrap(Session.class);
//                NativeQuery query = session.createNativeQuery(
//                        "INSERT INTO interview_chunks (chunk_text, embedding, metadata) VALUES (:chunk_text, :embedding, :metadata)");
//
//                query.setParameter("chunk_text", chunk);
//                query.setParameter("metadata", metadata);
//                query.setParameter("embedding", vectorParam, org.hibernate.type.SerializableType.INSTANCE); // still may wrap as bytea
//                query.executeUpdate();

                String sql = "INSERT INTO interview_chunks (chunk_text, metadata, embedding) VALUES (?, ?::jsonb, ?)";

                PGobject vectorObj = new PGobject();
                vectorObj.setType("vector");
                vectorObj.setValue(toPgVectorLiteral(embedding)); // "{0.1,0.2,...}"

                PGobject jsonObj = new PGobject();
                ObjectMapper objectMapper = new ObjectMapper();
                jsonObj.setType("jsonb");
                jsonObj.setValue(objectMapper.writeValueAsString(metadata));

                jdbc.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setString(1, chunk);
                    ps.setObject(2, jsonObj);
                    ps.setObject(3, vectorObj); // critical: use setObject with PGobject
                    return ps;
                });
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        System.out.println("PDF Embeddings Saved Successfully!");
    }
}
