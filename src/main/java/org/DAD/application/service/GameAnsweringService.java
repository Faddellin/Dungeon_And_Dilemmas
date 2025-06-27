package org.DAD.application.service;

import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.domain.entity.Answer.TextAnswer;

import java.util.UUID;

public interface GameAnsweringService {
    Boolean isAnswerCorrect(UUID questionId, UUID answerId) throws ExceptionWrapper;
    TextAnswer getCorrectAnswer(UUID questionId) throws ExceptionWrapper;
    void saveUserAnswer(String groupId, String playerId, String answerId) throws ExceptionWrapper;
    void sendAnswerResults(String groupId) throws ExceptionWrapper;
}
