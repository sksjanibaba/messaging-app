package com.example.ChatApp.controller;

import com.example.ChatApp.DTO.LoginRequestDTO;
import com.example.ChatApp.DTO.LoginResponseDTO;
import com.example.ChatApp.DTO.RegisterRequestDTO;
import com.example.ChatApp.DTO.UserDTO;
import com.example.ChatApp.Entity.User;
import com.example.ChatApp.Repository.UserRepository;
import com.example.ChatApp.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody RegisterRequestDTO registerRequestDTO){

        return ResponseEntity.ok(authenticationService.signup(registerRequestDTO));
    }
    @PostMapping("/login")
    public  ResponseEntity<UserDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDTO){
        LoginResponseDTO loginResponseDTO=authenticationService.login(loginRequestDTO);
        ResponseCookie responseCookie=ResponseCookie.from("JWT",loginResponseDTO.getToken())
        .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(1*60*60)
                .sameSite("strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,responseCookie.toString())
                .body(loginResponseDTO.getUserDTO());


    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return authenticationService.logout();
    }
    @GetMapping("/getonlineusers")
    public ResponseEntity<Map<String,Object>> getOnlineUsers(){
        return ResponseEntity.ok(authenticationService.getOnlineUsers());
    }
    @GetMapping("/getcurrentuser")
    public ResponseEntity<?> getCurrentUser(Authentication authentication){
        if(authentication==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("user not authorized");

        }
        String username=authentication.getName();
        User user=userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("user not found"));
        return ResponseEntity.ok(convertToUserDTO(user));
    }
    public UserDTO convertToUserDTO(User user){
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail((user.getEmail()));
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

}
