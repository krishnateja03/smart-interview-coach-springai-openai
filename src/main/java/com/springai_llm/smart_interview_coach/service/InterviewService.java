package com.springai_llm.smart_interview_coach.service;


import com.springai_llm.smart_interview_coach.dto.EvaluationRequest;
import com.springai_llm.smart_interview_coach.dto.EvaluationResponse;
import com.springai_llm.smart_interview_coach.dto.QuestionRequest;
import com.springai_llm.smart_interview_coach.dto.QuestionResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class InterviewService {


    private final ChatClient chatClient;

    public InterviewService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public QuestionResponse generateQuestion(QuestionRequest request) {
        String prompt = String.format("Generate a %s difficulty interview question for category: %s." +
                        "Generated Question should be a theoretical one with max limit of 20 words",
                request.difficulty(), request.category());

        String response = chatClient
                .prompt(prompt)
                .call()
                .content();
        return new QuestionResponse(response);
    }

    public EvaluationResponse evaluateAnswer(EvaluationRequest request) {
        String prompt = String.format(
                "You are an expert interview coach. Evaluate the following answer and suggest improvements if needed.\n\n" +
                        "Question: %s\n\n" +
                        "Answer: %s\n\n" +
                        "Please provide detailed feedback in lesser than 50 words:",
                request.question(), request.answer()
        );

        String response = chatClient
                .prompt(prompt)
                .call()
                .content();
        return new EvaluationResponse(response);
    }
}

