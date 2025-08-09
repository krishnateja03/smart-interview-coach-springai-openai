package com.springai_llm.smart_interview_coach.controller;


import com.springai_llm.smart_interview_coach.dto.EvaluationRequest;
import com.springai_llm.smart_interview_coach.dto.EvaluationResponse;
import com.springai_llm.smart_interview_coach.dto.QuestionRequest;
import com.springai_llm.smart_interview_coach.dto.QuestionResponse;
import com.springai_llm.smart_interview_coach.service.InterviewService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @RequestMapping(path = "/questions", method = RequestMethod.GET)
    public QuestionResponse getQuestion(@RequestParam String category,
                                        @RequestParam String difficulty) {
        return interviewService.generateQuestion(new QuestionRequest(category, difficulty));
    }

    @RequestMapping(path = "/evaluate", method = RequestMethod.POST)
    public EvaluationResponse evaluateAnswer(@RequestBody EvaluationRequest request) {
        return interviewService.evaluateAnswer(request);
    }
}
