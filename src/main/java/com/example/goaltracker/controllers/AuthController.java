package com.example.goaltracker.controllers;

 import com.example.goaltracker.security.JwtTokenProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

     @Autowired
     private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication.getName());

            Map<String, String> response = Map.of(
                    "message", "User authenticated successfully",
                    "status", "200",
                    "token", token
            );

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            Map<String, String> response = Map.of(
                    "message", "Invalid email/password supplied",
                    "status", "401"
            );

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @Getter
    @Setter
    public static class LoginRequest {
        private String email;
        private String password;
    }
}
