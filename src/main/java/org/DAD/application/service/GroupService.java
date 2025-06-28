package org.DAD.application.service;

import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.Connection.PlayerIsReadyMessage;
import org.DAD.application.model.Connection.PlayerLeftMessage;
import org.DAD.application.model.Group.GroupModel;

import java.util.UUID;

public interface GroupService {
    GroupModel createGroup(UUID userId) throws ExceptionWrapper;

    GroupModel joinGroup(UUID userId, String code) throws ExceptionWrapper;

    void selectQuiz(UUID userId, UUID quizId) throws ExceptionWrapper;

    void setPlayerIsReady(PlayerIsReadyMessage playerIsReadyMessage)throws ExceptionWrapper;
    void setPlayerIsReady2(PlayerIsReadyMessage playerIsReadyMessage)throws ExceptionWrapper;

    void leftGroup(PlayerLeftMessage playerLeftMessage) throws ExceptionWrapper;
}
