package org.DAD.application.service;

import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.Answer.AnswerCreateModel;
import org.DAD.application.model.Question.QuestionCreateModel;
import org.DAD.application.model.Quiz.QuizCreateModel;
import org.DAD.application.model.Quiz.QuizDetailModel;
import org.DAD.application.model.Quiz.QuizFiltersModel;
import org.DAD.application.model.Quiz.QuizModel;
import org.DAD.application.model.Quiz.QuizPagedListModel;

import java.util.UUID;

public interface QuizService {
    UUID createQuiz(UUID userId, QuizCreateModel quizCreateModel) throws ExceptionWrapper;

    void deleteQuiz(UUID userId, UUID quizId) throws ExceptionWrapper;

    QuizModel getQuizById(UUID quizId) throws ExceptionWrapper;

    QuizDetailModel getQuizDetailById(UUID userId, UUID quizId) throws ExceptionWrapper;

    QuizPagedListModel getQuizzesByFilters(QuizFiltersModel quizFiltersModel) throws ExceptionWrapper;

    UUID createQuestion(UUID userId, UUID quizId, QuestionCreateModel questionCreateModel) throws ExceptionWrapper;

    void deleteQuestion(UUID userId, UUID questionId) throws ExceptionWrapper;

    UUID createAnswer(UUID userId, UUID questionId, AnswerCreateModel answerCreateModel) throws ExceptionWrapper;
    void setCorrectAnswer(UUID userId, UUID questionId, UUID answerId) throws ExceptionWrapper;

    void deleteAnswer(UUID userId, UUID answerId) throws ExceptionWrapper;
}
