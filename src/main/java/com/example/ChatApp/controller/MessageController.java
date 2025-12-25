package com.example.ChatApp.controller;

import com.example.ChatApp.Entity.Message;
import com.example.ChatApp.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;
    @GetMapping("/private")
    public ResponseEntity<List<Message>> getPrivateMessage(@RequestParam String user1, @RequestParam String user2) {
        List<Message> message = messageRepository.findPrivateMessagesBetweenTwoUsers(user1, user2);
        return ResponseEntity.ok(message);
    }
}
