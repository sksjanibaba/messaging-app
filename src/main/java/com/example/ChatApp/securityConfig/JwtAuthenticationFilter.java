package com.example.ChatApp.securityConfig;

import com.example.ChatApp.Service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter{
    @Autowired
    private JwtUtilies jwtUtilies;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    protected  void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String AuthHeader=req.getHeader("Authorization");
        String username=null;
        String jwt=null;
        if(AuthHeader!=null && AuthHeader.startsWith("Bearer ")){
            jwt=AuthHeader.substring(7);
            username=jwtUtilies.getUsernameFromJwts(jwt);
        }
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails=userDetailsService.loadUserByUsername(username);
            if(jwtUtilies.validateToken(jwt)){
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }
        filterChain.doFilter(req,res);
    }
}
