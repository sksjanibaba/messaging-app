package com.example.ChatApp.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class UserDTO {


        private Long id;
        private String username;

        private String email;
        private boolean isOnline;


        public boolean isOnline() {
            return isOnline;
        }

        public void setOnline(boolean online) {
            isOnline = online;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }




        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }


    }


