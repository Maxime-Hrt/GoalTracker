package com.example.goaltracker.controllers;

import com.example.goaltracker.entities.User;
import com.example.goaltracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @QueryMapping
    public List<User> users() {
        return userService.findAllUsers();
    }

    @QueryMapping
    public Optional<User> userById(@Argument Long id) {
        return userService.findUserById(id);
    }

    @QueryMapping
    public Optional<User> userByUsername(@Argument String username) {
        return userService.findUserByUsername(username);
    }

    @MutationMapping
    public ResponseEntity<Map<String, Object>> createUser(@Argument String email,
                                                          @Argument String username,
                                                          @Argument String password,
                                                          @Argument String role) {
        try {
            User user = new User(email, username, password, role);
            User savedUser = userService.saveUser(user);

            Map<String, Object> response = Map.of(
                    "status", "success",
                    "message", "User created successfully",
                    "data", savedUser
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = Map.of(
                    "status", "error",
                    "message", "Failed to create user: " + e.getMessage()
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
