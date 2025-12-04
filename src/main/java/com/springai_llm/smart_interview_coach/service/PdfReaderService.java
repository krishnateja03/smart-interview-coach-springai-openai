package com.springai_llm.smart_interview_coach.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class PdfReaderService {

    public String readKnowledgeBase() {
        try {
            File file = new File("src/main/resources/rag_test_interview_knowledge_base.pdf");
            try (RandomAccessReadBufferedFile rar = new RandomAccessReadBufferedFile(file);
                 PDDocument document = Loader.loadPDF(rar)) {

                PDFTextStripper stripper = new PDFTextStripper();
                return stripper.getText(document);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load PDF", e);
        }
    }
}

