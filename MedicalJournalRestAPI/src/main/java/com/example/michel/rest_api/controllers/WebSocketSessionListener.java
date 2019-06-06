package com.example.michel.rest_api.controllers;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.*;

@Component
public class WebSocketSessionListener {

    @EventListener
    public void connectionEstablished(SessionConnectedEvent sce)
    {

    }

    @EventListener
    public void webSockectDisconnect(SessionDisconnectEvent sde)
    {

    }

}
