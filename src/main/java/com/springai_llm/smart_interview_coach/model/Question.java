package com.springai_llm.smart_interview_coach.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    private String id;
    private String category;
    private String difficulty;
    private String text;
}