package com.springai_llm.smart_interview_coach.controller;

import com.springai_llm.smart_interview_coach.service.PdfReaderService;
import com.springai_llm.smart_interview_coach.service.VectorStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class IngestionController {

    private final PdfReaderService pdfReaderService;
    private final VectorStoreService vectorStoreService;

    @RequestMapping(path = "/ingest/pdf", method = RequestMethod.POST)
    public String ingestPdf() {
        String text = pdfReaderService.readKnowledgeBase();
        vectorStoreService.storePdfKnowledgeBase(text);
        return "Ingestion complete!";
    }
}

