package org.DAD.application.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.Connection.FromFront.AnswerMessage;
import org.DAD.application.model.Connection.MessageWrapper;
import org.DAD.application.model.Connection.PlayerIsReadyMessage;
import org.DAD.application.model.Connection.PlayerLeftMessage;
import org.DAD.application.service.GameAnsweringService;
import org.DAD.application.service.GroupService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@Controller
@AllArgsConstructor
public class ConnectionController {
    private final GameAnsweringService gameAnsweringService;
    private final SimpMessagingTemplate messagingTemplate;
    private final GroupService _groupService;

    @SubscribeMapping("/topic/game/{groupId}")
    public void confirmSubscription(@DestinationVariable UUID groupId, Principal principal) {
        log.info("User {} subscribed to group {}", principal.getName(), groupId);
    }

    @MessageMapping("/game/{groupId}")
    public void handleAnswer(@DestinationVariable String groupId, MessageWrapper message) throws ExceptionWrapper {
        
        if (message instanceof AnswerMessage answerMessage) {
            gameAnsweringService.saveUserAnswer(groupId, message.getPlayerId(), answerMessage.getAnswerId());
        } 
        else if(message instanceof PlayerLeftMessage playerLeftMessage) {
            _groupService.leftGroup(playerLeftMessage);
        }
        else if(message instanceof PlayerIsReadyMessage playerIsReadyMessage) {
            _groupService.setPlayerIsReady(playerIsReadyMessage);
        }
    }
}