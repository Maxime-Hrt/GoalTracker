package com.example.goaltracker.controllers;

import com.example.goaltracker.entities.User;
import com.example.goaltracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
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
    public User createUser(@Argument String email,
                           @Argument String username,
                           @Argument String password,
                           @Argument String role) {
        User user = new User(email, username, password, role);
        return userService.saveUser(user);
    }
}
