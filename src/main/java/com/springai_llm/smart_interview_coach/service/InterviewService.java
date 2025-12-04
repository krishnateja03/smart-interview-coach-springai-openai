package com.springai_llm.smart_interview_coach.service;


import com.springai_llm.smart_interview_coach.dto.EvaluationRequest;
import com.springai_llm.smart_interview_coach.dto.EvaluationResponse;
import com.springai_llm.smart_interview_coach.dto.QuestionRequest;
import com.springai_llm.smart_interview_coach.dto.QuestionResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class InterviewService {

    private final ChatClient chatClient;
    private final RagContextService ragContextService;

    public InterviewService(ChatClient.Builder chatClient, RagContextService ragContextService) {
        this.chatClient = chatClient.build();
        this.ragContextService = ragContextService;
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
        String context = ragContextService.retrieveContext("QUESTION: " + request.question() + "\n\n" + "ANSWER: " + request.answer());
        String promptMsg = String.format(
                "You are an expert interview coach. \n\n" +
                        "Use the following context when evaluating the user's answer:\n" +
                        "---------------------\n" +
                        "%s\n" +
                        "---------------------\n" +
                        "Evaluate the following answer and suggest improvements if needed.\n\n" +
                        "Question: %s\n\n" +
                        "Answer: %s\n\n" +
                        "Please provide detailed feedback in lesser than 50 words:",
                context, request.question(), request.answer()
        );

        UserMessage userMessage = new UserMessage(promptMsg);
        Prompt promptObj = new Prompt(userMessage);
        String response = chatClient
                .prompt(promptObj)
                .call()
                .content();

        return new EvaluationResponse(response);
    }
}

