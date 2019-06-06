package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.auxiliary_models.request_bodies.UpdateMeasurementReminderBody;
import com.example.michel.rest_api.models.auxiliary_models.request_bodies.UpdatePillReminderBody;
import com.example.michel.rest_api.models.auxiliary_models.test_web_socket.Message;
import com.example.michel.rest_api.models.auxiliary_models.test_web_socket.OutputMessage;
import com.example.michel.rest_api.models.auxiliary_models.websocket_responce_bodies.PillAndMeasurementEntriesUpdateBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload Message msg, SimpMessageHeaderAccessor headerAccessor, Principal principal) {
        System.out.println(msg.getText() + " " + principal.getName());
        Authentication h = SecurityContextHolder.getContext().getAuthentication();
        OutputMessage out = new OutputMessage(
                msg.getFrom(),
                msg.getText(),
                new SimpleDateFormat("HH:mm").format(new Date()));
        //simpMessagingTemplate.convertAndSend("/user/queue/notify", out);
        simpMessagingTemplate.convertAndSendToUser(principal.getName(),"/queue/notify", out);
    }

    @MessageMapping("/reminderEntryChanged")
    public void reminderEntryChanged(@Payload Message msg, SimpMessageHeaderAccessor headerAccessor, Principal principal) {
        System.out.println(msg.getText() + " " + principal.getName());
        Authentication h = SecurityContextHolder.getContext().getAuthentication();
        OutputMessage out = new OutputMessage(
                msg.getFrom(),
                msg.getText(),
                new SimpleDateFormat("HH:mm").format(new Date()));
        //simpMessagingTemplate.convertAndSend("/user/queue/notify", out);
        simpMessagingTemplate.convertAndSendToUser(principal.getName(),"/queue/notify", out);
    }

    @MessageMapping("/updatePillReminderWS")
    public void updatePillReminder(@Payload UpdatePillReminderBody updatePillReminderBody, Principal principal){
        PillAndMeasurementEntriesUpdateBody updateBody = new PillAndMeasurementEntriesUpdateBody(
                updatePillReminderBody, null);

        simpMessagingTemplate.convertAndSendToUser(principal.getName(),"/queue/reminderEntryUpdated", updateBody);
        //int r = pillReminderEntryFService.updateIsDonePillReminderEntry(updatePillReminderBody);
    }

    @MessageMapping("/updateMeasurementReminderWS")
    public void updateMeasurementReminder(@Payload UpdateMeasurementReminderBody updateMeasurementReminderBody, Principal principal) {
        PillAndMeasurementEntriesUpdateBody updateBody = new PillAndMeasurementEntriesUpdateBody(
                null, updateMeasurementReminderBody);
        simpMessagingTemplate.convertAndSendToUser(principal.getName(),"/queue/reminderEntryUpdated", updateBody);
        //int r = measurementReminderEntryFService.updateIsDoneMeasurementReminderEntry(updateMeasurementReminderBody);
    }

}
