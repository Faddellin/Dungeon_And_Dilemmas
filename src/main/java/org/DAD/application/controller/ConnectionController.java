package org.DAD.application.controller;

import lombok.AllArgsConstructor;
import org.DAD.application.handler.ExceptionWrapper;
import org.DAD.application.model.Connection.FromFront.AnswerMessage;
import org.DAD.application.model.Connection.FromBack.AnswerResultMessage;
import org.DAD.application.model.Connection.FromBack.GameStartedMessage;
import org.DAD.application.model.Connection.MessageWrapper;
import org.DAD.application.service.GameAnsweringService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

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
        else if (message instanceof GameStartedMessage) {
            
        }
    }
}