package com.example.ChatApp.listener;

import com.example.ChatApp.Entity.Message;
import com.example.ChatApp.Service.UserService;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class webSocketListener {
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    @Autowired
    private UserService userService;
    private static final Logger logger= LoggerFactory.getLogger(webSocketListener.class);
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event){
       logger.info("Connected to web socket");
    }
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor=StompHeaderAccessor.wrap(event.getMessage());
        String username=headerAccessor.getSessionAttributes().get("username").toString();
System.out.println("User disconnected from websocket");
        userService.setUserOnlineStatus(username,false);
        Message message=new Message();
        message.setType(Message.MessageType.LEAVING);
        message.setSender(username);
        messagingTemplate.convertAndSend("/topic/public",message);

    }
}
