package org.DAD.domain.service;

import org.DAD.application.model.Connection.MessageWrapper;
import org.DAD.application.service.ConnectionService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConnectionServiceImpl implements ConnectionService {

    private final SimpMessagingTemplate messagingTemplate;

    public ConnectionServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessageToGroup(UUID groupId, MessageWrapper messageWrapper) {

        messagingTemplate.convertAndSend("/topic/game/" + groupId, messageWrapper);
    }

}
