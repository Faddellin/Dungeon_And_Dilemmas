package org.DAD.application.service;

import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.Answer.AnswerCreateModel;
import org.DAD.application.model.Question.QuestionCreateModel;
import org.DAD.application.model.Quiz.QuizCreateModel;
import org.DAD.application.model.Quiz.QuizFiltersModel;
import org.DAD.application.model.Quiz.QuizModel;
import org.DAD.application.model.Quiz.QuizPagedListModel;

import java.util.UUID;

public interface GroupService {
    String createGroup(UUID userId) throws ExceptionWrapper;

    void joinGroup(UUID userId, String code) throws ExceptionWrapper;

    void leftGroup(UUID userId) throws ExceptionWrapper;
}
