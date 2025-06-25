package org.DAD.application.controller;

import org.DAD.application.model.Connection.MessageWrapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ConnectionController {

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public MessageWrapper sendMessage(MessageWrapper message) {
        return message;
    }

}