package com.example.ChatApp.Entity;

import jakarta.persistence.*;

import java.awt.*;
import java.time.LocalDateTime;

@Entity
@Table(name="messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sender;
    private String recepient;
    private String content;
    @Enumerated(EnumType.STRING)
    private MessageType type;
    @Column(nullable=false)
    private LocalDateTime stampTime;
    private String color;

    public enum MessageType{
        CHAT,PRIVATE_MESSAGE,JOIN,LEAVING,TYPING
    }

    public MessageType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecepient() {
        return recepient;
    }

    public void setRecepient(String recepient) {
        this.recepient = recepient;
    }



    public LocalDateTime getStampTime() { return stampTime; }
    public void setStampTime(LocalDateTime stampTime) { this.stampTime = stampTime; }
}
