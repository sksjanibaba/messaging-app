package com.example.ChatApp.securityConfig;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtilies {
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private int ExpirationJwt= 86400000;

 public String generateTokenJwt(String username){
     return Jwts.builder()
             .setIssuedAt(new Date())
             .setSubject(username)
             .setExpiration(new Date(System.currentTimeMillis()+ExpirationJwt))
             .signWith(key)
             .compact();
 }
 public String getUsernameFromJwts(String token){
     return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();

 }
 public boolean validateToken(String token){
     try{
         Jwts.parser().setSigningKey(key).parseClaimsJws(token);
         return true;
     }
     catch(JwtException e){
         e.printStackTrace();
     }
     return false;
 }
}
