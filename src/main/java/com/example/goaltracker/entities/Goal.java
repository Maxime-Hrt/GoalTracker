package com.example.goaltracker.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "goal")
@Data
@RequiredArgsConstructor
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalStatus status;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime targetDate;

    @Enumerated(EnumType.STRING)
    private Color color;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Goal(String title, String description, GoalStatus status, LocalDateTime createdDate, LocalDateTime targetDate, User user) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
        this.targetDate = targetDate;
        this.user = user;
    }
}
