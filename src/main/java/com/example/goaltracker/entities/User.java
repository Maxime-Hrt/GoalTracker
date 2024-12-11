package com.example.goaltracker.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "app_user")
@Data // @Data = @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor + @RequiredArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String token;

    public User(String email, String username, String password, String role, String token) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.token = token;
    }
}
