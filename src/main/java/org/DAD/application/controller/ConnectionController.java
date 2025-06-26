package org.DAD.application.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.Connection.FromFront.AnswerMessage;
import org.DAD.application.model.Connection.MessageWrapper;
import org.DAD.application.service.GameAnsweringService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@AllArgsConstructor
public class ConnectionController {
    private final GameAnsweringService gameAnsweringService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/game/{groupId}")
    public void handleAnswer(@DestinationVariable String groupId, MessageWrapper message) throws ExceptionWrapper {
        
        if (message instanceof AnswerMessage answerMessage) {
            gameAnsweringService.saveUserAnswer(groupId, message.getPlayerId(), answerMessage.getAnswerId());
        } 
        else {
            log.warn("Received unknown message type: {}", message.getClass().getSimpleName());
        }
    }
}