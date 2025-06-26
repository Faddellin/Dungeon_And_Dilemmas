package org.DAD.application.config;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.DAD.application.security.JwtAuthentication;
import org.DAD.application.security.JwtProvider;
import org.DAD.application.security.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtProvider jwtProvider;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Префикс для подписок
        config.setApplicationDestinationPrefixes("/app"); // Префикс для отправки сообщений
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // Эндпоинт для подключения
                .setAllowedOriginPatterns("*") // Разрешить все origins (для разработки)
                .withSockJS(); // Поддержка SockJS для старых браузеров
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                System.out.println("WebSocket Message: " + message);

                if (StompCommand.CONNECT.equals(accessor.getCommand()) ||
                    StompCommand.SEND.equals(accessor.getCommand()) ||
                    StompCommand.MESSAGE.equals(accessor.getCommand())) {
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);
                        if (jwtProvider.validateAccessToken(token)) {
                            final Claims claims = jwtProvider.getAccessClaims(token);
                            final JwtAuthentication authentication = JwtUtils.generate(claims);
                            authentication.setAuthenticated(true);
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            accessor.setUser(authentication);
                        }else{
                            throw new RuntimeException();
                        }
                    }
                }
                return message;
            }
        });
    }

}



