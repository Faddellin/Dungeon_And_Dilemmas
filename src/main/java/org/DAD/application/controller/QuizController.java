package org.DAD.application.controller;

import org.DAD.application.model.Quiz.QuizCreateModel;
import org.DAD.application.model.Quiz.QuizFiltersModel;
import org.DAD.application.service.QuizService;
import org.DAD.domain.entity.Answer.Answer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class QuizController {

    private final QuizService _quizService;

    public QuizController(QuizService quizService) {
        _quizService = quizService;
    }

    @PostMapping(path = "quizzes/")
    public void CreateQuiz(
            @RequestBody QuizCreateModel quizCreateModel
    ) {

    }

    @GetMapping(path = "quizzes/{id}")
    public void GetQuizById(
            @PathVariable UUID id
    ) {

    }

    @GetMapping(path = "quizzes/")
    public void GetQuizzesByFilters(
            @RequestParam QuizFiltersModel quizFiltersModel
    ) {

    }

    @PostMapping(path = "quizzes/{id}/questions")
    public void CreateQuizQuestion(
            @PathVariable UUID id,
            @RequestBody QuizCreateModel quizCreateModel
    ){

    }

    @PostMapping(path = "quizzes/questions/{id}/answers")
    public void CreateQuestionAnswer(
            @PathVariable UUID id,
            @RequestBody Answer answer
    ){
        var a = 5;
    }

}
