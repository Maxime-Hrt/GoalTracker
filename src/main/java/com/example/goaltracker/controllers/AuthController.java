package com.example.goaltracker.controllers;

import com.example.goaltracker.dto.LoginRequestDTO;
import com.example.goaltracker.dto.TokenRequestDTO;
import com.example.goaltracker.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
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

    @GetMapping("/validate-token")
    public ResponseEntity<Map<String, String>> validateToken(@RequestParam String token) {
        try {
            boolean isValid = jwtTokenProvider.validateToken(token);
            if (isValid) {
                String username = jwtTokenProvider.getUsernameFromJWT(token);
                Map<String, String> response = Map.of(
                        "message", "Token is valid",
                        "status", "200",
                        "username", username
                );
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = Map.of(
                        "message", "Token is invalid",
                        "status", "401"
                );
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            Map<String, String> response = Map.of(
                    "message", "Error while validating token",
                    "status", "500"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody TokenRequestDTO tokenRequest) {
        try {
            String oldToken = tokenRequest.getToken();
            if (jwtTokenProvider.validateToken(oldToken)) {
                Map<String, String> response = Map.of(
                        "message", "Token is still valid, no need to refresh",
                        "status", "200",
                        "token", oldToken
                );
                return ResponseEntity.ok(response);
            } else if (jwtTokenProvider.canTokenBeRefreshed(oldToken)) {
                System.out.println("Refreshing token");
                String username = jwtTokenProvider.getUsernameFromJWT(oldToken);
                String newToken = jwtTokenProvider.generateToken(username);
                Map<String, String> response = Map.of(
                        "message", "Token refreshed successfully",
                        "status", "200",
                        "token", newToken
                );
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = Map.of(
                        "message", "Token is invalid and cannot be refreshed",
                        "status", "400"
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = Map.of(
                    "message", "Error while refreshing token",
                    "status", "500"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
