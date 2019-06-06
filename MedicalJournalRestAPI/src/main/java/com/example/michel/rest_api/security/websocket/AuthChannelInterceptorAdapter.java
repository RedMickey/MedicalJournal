package com.example.michel.rest_api.security.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {

    @Autowired
    private WebSocketAuthenticatorService webSocketAuthenticatorService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            /*if (true){
                throw new AuthenticationCredentialsNotFoundException("Username was null or empty.");
            }*/

            //String login = accessor.getFirstNativeHeader("login");
            String tokenStr = accessor.getFirstNativeHeader("access_token");

            try {
                accessor.setUser(webSocketAuthenticatorService.getAuthenticatedOrFail(tokenStr));
            }
            catch (Exception ex){
                return message;
            }
            /*accessor.setUser(new UsernamePasswordAuthenticationToken(
                    login,
                    null,
                    Collections.singleton((GrantedAuthority) () -> "ROLE_1")
            ));*/
        }

        return message;
    }
}
