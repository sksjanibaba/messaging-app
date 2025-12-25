package com.example.ChatApp.controller;


import com.example.ChatApp.Entity.Message;
import com.example.ChatApp.Repository.MessageRepository;
import com.example.ChatApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller  // <-- Use @Controller for WebSocket endpoints
public class ChatController {
@Autowired
private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageRepository messageRepository;

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(@Payload Message chatMessage,
                           SimpMessageHeaderAccessor headerAccessor) {
        if (userService.userExists(chatMessage.getSender())) {

            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
            userService.setUserOnlineStatus(chatMessage.getSender(), true);

            System.out.println("User added successfully " + chatMessage.getSender()
                    + " with session id " + headerAccessor.getSessionId());

            chatMessage.setStampTime(LocalDateTime.now());
            if (chatMessage.getContent()==null) {
                chatMessage.setContent("");
            }
            messageRepository.save(chatMessage);
        }
        return chatMessage;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message chatMessage) {
        if (userService.userExists(chatMessage.getSender())) {
            if (chatMessage.getStampTime() == null) {
                chatMessage.setStampTime(LocalDateTime.now());
            }


            if (chatMessage.getContent() == null) {
                chatMessage.setContent((""));
            }
            return messageRepository.save(chatMessage);
        }
        return null;
    }

    @MessageMapping("/chat.sendPrivateMessage")
    public void sendPrivateMessage(@Payload Message chatMessage,
                                   SimpMessageHeaderAccessor headerAccessor) {
        if (userService.userExists(chatMessage.getSender()) && userService.userExists(chatMessage.getRecepient())){
            if (chatMessage.getStampTime() == null) {
                chatMessage.setStampTime(LocalDateTime.now());
            }


            if (chatMessage.getContent() == null) {
                chatMessage.setContent((""));
            }
            chatMessage.setType(Message.MessageType.PRIVATE_MESSAGE);
            Message saveMessage= messageRepository.save(chatMessage);
            System.out.println("Message save successfully with id "+saveMessage.getId());
            try {
                String recepientDestination = "/user/" + chatMessage.getRecepient() + "/queue/private";
                System.out.println("Sending mesage to recepient destination" + recepientDestination);
                messagingTemplate.convertAndSend(recepientDestination, saveMessage);
                String senderDestination = "/user" + chatMessage.getSender() + "/queue/private";
                System.out.println("Sending message to sender destination" + senderDestination);
                messagingTemplate.convertAndSend(senderDestination, saveMessage);
            }
            catch(Exception e){
                System.out.println("Error occured while sendind the message"+e.getMessage());
            }

        }
        else{
            System.out.println("Error:sender"+chatMessage.getSender()+" or recepient"+chatMessage.getRecepient()+"does not exist");
        }
    }
}
