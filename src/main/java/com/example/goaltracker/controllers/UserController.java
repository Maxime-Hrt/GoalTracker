package com.example.goaltracker.controllers;

import com.example.goaltracker.dto.LoginDTO;
import com.example.goaltracker.dto.UserDTO;
import com.example.goaltracker.entities.User;
import com.example.goaltracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody UserDTO userDTO) {
        if (userDTO.password() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si pas de mot de passe
        }
        String token = userService.generateToken(userDTO.username(), userDTO.password());
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Si erreur lors de la génération du token
        }

        User user = new User(userDTO.email(), userDTO.username(), userDTO.password(), "USER", token);

        User saveUser = userService.saveUser(user);

        // Remove password from response
        saveUser.setPassword(null);

        return new ResponseEntity<>(saveUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        // Remove password from response
        List<User> users = userService.findAllUsers();
        users.forEach(user -> user.setPassword(null));

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        // Remove password from response
        user.ifPresent(value -> value.setPassword(null));
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> findUserByUsername(@PathVariable String username) {
        System.out.println("username: " + username);
        Optional<User> user = userService.findUserByUsername(username);
        // Remove password from response
        user.ifPresent(value -> value.setPassword(null));
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> findUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.findUserByEmail(email);
        // Remove password from response
        user.ifPresent(value -> value.setPassword(null));
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> userOptional = userService.findUserById(id);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User userToUpdate = userOptional.get();
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setRole(user.getRole());
        userToUpdate.setToken(user.getToken());
        return new ResponseEntity<>(userService.saveUser(userToUpdate), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginDTO loginDTO) {
        System.out.println("Login attempt for username: " + loginDTO.email());

        Optional<User> user = userService.findUserByEmail(loginDTO.email());
        if (user.isEmpty()) {
            System.out.println("User not found: " + loginDTO.email());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        boolean passwordValid = userService.checkPassword(loginDTO.email(), loginDTO.password());
        if (!passwordValid) {
            System.out.println("Invalid password for username: " + loginDTO.email());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User foundUser = user.get();
        foundUser.setPassword(null);

        System.out.println("Login successful for username: " + loginDTO.email());
        return new ResponseEntity<>(foundUser, HttpStatus.OK);
    }

}
