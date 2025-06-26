package org.DAD.application.service;

import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.Connection.MessageWrapper;
import org.DAD.application.model.Group.GroupModel;

import java.util.UUID;

public interface ConnectionService {
    void sendMessageToGroup(UUID groupId, MessageWrapper messageWrapper);
}
