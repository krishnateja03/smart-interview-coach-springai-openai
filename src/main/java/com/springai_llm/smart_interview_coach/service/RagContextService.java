package com.springai_llm.smart_interview_coach.service;

import com.springai_llm.smart_interview_coach.dto.RagChunkDto;
import com.springai_llm.smart_interview_coach.repository.DocumentEmbeddingRepository;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.springai_llm.smart_interview_coach.util.PGVectorUtils.toPgVectorLiteral;

@Service
public class RagContextService {
    private final EmbeddingService embeddingService;
    private final JdbcTemplate jdbc;

    @Value("${rag.similarity.search.topk}")
    private Integer topK;

    public RagContextService(EmbeddingService embeddingService, JdbcTemplate jdbc) {
        this.embeddingService = embeddingService;
        this.jdbc = jdbc;
    }

    public String retrieveContext(String query) {

        float[] embedding = embeddingService.embed(query);

        try {
//            PGobject vectorParam = new PGobject();
//            vectorParam.setType("vector");
//
//            StringBuilder sb = new StringBuilder("[");
//            for (int i = 0; i < embedding.length; i++) {
//                sb.append(embedding[i]);
//                if (i < embedding.length - 1) sb.append(",");
//            }
//            sb.append("]");
//            vectorParam.setValue(sb.toString());
//
//            // Native query: select only the mapped columns
//            List<RagChunkDto> result = em.createNativeQuery(
//                            "SELECT id, chunk_text " +
//                                    "FROM interview_chunks " +
//                                    "ORDER BY embedding <-> CAST(? AS vector) " +
//                                    "LIMIT ?",
//                            RagChunkDto.class)
//                    .setParameter(1, vectorParam)
//                    .setParameter(2, 3)
//                    .getResultList();
//            return result.stream()
//                .map(RagChunkDto::getChunkText)
//                .collect(Collectors.joining("\n\n"));

            String sql = "SELECT id, chunk_text " +
                    "FROM interview_chunks " +
                    "ORDER BY embedding <-> ? " +
                    "LIMIT ?";

            PGobject vectorObj = new PGobject();
            vectorObj.setType("vector");
            vectorObj.setValue(toPgVectorLiteral(embedding));

            List<RagChunkDto> result = jdbc.query(connection -> {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setObject(1, vectorObj);
                        ps.setInt(2, topK);
                        return ps;
                    }, (rs, rowNum) ->
                            new RagChunkDto(
                                    rs.getLong("id"),
                                    rs.getString("chunk_text")
                            )
            );

            return result.stream()
                    .map(RagChunkDto::getChunkText)
                    .collect(Collectors.joining("\n\n"));

        } catch (SQLException e) {
            System.out.println(e);
        }

        return null;
    }
}
