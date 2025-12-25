package com.example.ChatApp.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private String token;
    private UserDTO userDTO;
}
