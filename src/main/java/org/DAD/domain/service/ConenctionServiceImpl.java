package org.DAD.domain.service;

import jakarta.persistence.EntityNotFoundException;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.Connection.FromBack.AnswerResultMessage;
import org.DAD.application.model.Connection.FromBack.GameStartedMessage;
import org.DAD.application.model.Connection.FromFront.AnswerMessage;
import org.DAD.application.model.Connection.MessageWrapper;
import org.DAD.application.model.Group.GroupModel;
import org.DAD.application.repository.GroupRepository;
import org.DAD.application.repository.UserRepository;
import org.DAD.application.service.ConnectionService;
import org.DAD.application.service.GroupService;
import org.DAD.application.util.CodeGenerator;
import org.DAD.domain.entity.Group.ChatGroup;
import org.DAD.domain.entity.User.User;
import org.DAD.domain.mapper.GroupMapper;
import org.apache.coyote.BadRequestException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConenctionServiceImpl implements ConnectionService {

    private final SimpMessagingTemplate messagingTemplate;

    public ConenctionServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessageToGroup(UUID groupId, MessageWrapper messageWrapper) {

        messagingTemplate.convertAndSend("/topic/game/" + groupId, messageWrapper);
    }

}
