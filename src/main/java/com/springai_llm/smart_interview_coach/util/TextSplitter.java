package com.springai_llm.smart_interview_coach.util;

import java.util.ArrayList;
import java.util.List;

public class TextSplitter {
    public static List<String> splitIntoChunks(String text, int chunkSize) {
        List<String> chunks = new ArrayList<>();

        for (int i = 0; i < text.length(); i += chunkSize) {
            chunks.add(text.substring(i, Math.min(text.length(), i + chunkSize)));
        }

        return chunks;
    }
}
