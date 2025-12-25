package com.example.ChatApp.Service;

import com.example.ChatApp.DTO.LoginRequestDTO;
import com.example.ChatApp.DTO.LoginResponseDTO;
import com.example.ChatApp.DTO.RegisterRequestDTO;
import com.example.ChatApp.DTO.UserDTO;
import com.example.ChatApp.Entity.User;
import org.springframework.http.HttpHeaders;

import com.example.ChatApp.Repository.UserRepository;
import com.example.ChatApp.securityConfig.JwtUtilies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtilies jwtUtilies;

    public UserDTO signup(RegisterRequestDTO registerRequestDTO ){
        if(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()){
            throw new RuntimeException("username is already in use");
        }
        User user=new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setEmail(registerRequestDTO.getEmail());
        User saveUser=userRepository.save(user);
        return convertToUserDTO(user);
    }
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
        User user=userRepository.findByUsername(loginRequestDTO.getUsername()).orElseThrow(()->new RuntimeException("username not found"));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(),loginRequestDTO.getPassword()));
        String jwtToken=jwtUtilies.generateTokenJwt(user.getUsername());
        return LoginResponseDTO.builder().token(jwtToken).userDTO(convertToUserDTO(user)).build();
    }
    public ResponseEntity<String> logout(){
        ResponseCookie responseCookie=ResponseCookie.from("JWT")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(1*60*60)
                .sameSite("strict")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,responseCookie.toString()).body("Logged out successfully");
    }
    public Map<String,Object> getOnlineUsers(){
        List<User> usersList=userRepository.findByIsOnlineTrue();
        Map<String,Object> onlineUsers=usersList.stream().collect(Collectors.toMap(User::getUsername, user->user));
        return onlineUsers;
    }
    public UserDTO convertToUserDTO(User user){
        UserDTO userDTO=new UserDTO();

        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}
