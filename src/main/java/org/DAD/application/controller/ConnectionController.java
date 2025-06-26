package org.DAD.application.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.DAD.application.model.Connection.FromFront.AnswerMessage;
import org.DAD.application.model.Connection.FromBack.AnswerResultMessage;
import org.DAD.application.model.Connection.FromBack.GameStartedMessage;
import org.DAD.application.model.Connection.MessageWrapper;
import org.DAD.application.security.JwtAuthentication;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class ConnectionController {

    private final SimpMessagingTemplate messagingTemplate;

    public ConnectionController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/game/{groupId}")
    @SecurityRequirement(name = "JWT")
    public void handleAnswer(@DestinationVariable Integer groupId, MessageWrapper message) {

        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        var a = SecurityContextHolder.getContext();
        if (message instanceof AnswerMessage) {
            
        } else if (message instanceof GameStartedMessage) {
            
        }
        AnswerResultMessage result = new AnswerResultMessage();
        result.setCorrect(true);
        result.setCorrectAnswer("FSDFSFS");

        messagingTemplate.convertAndSend("/topic/game/" + groupId, result);
    }

}