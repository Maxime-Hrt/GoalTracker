package com.example.goaltracker.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Data
@RequiredArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean isCompleted;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    public Task(String title, boolean isCompleted, LocalDateTime createdDate, Goal goal) {
        this.title = title;
        this.isCompleted = isCompleted;
        this.createdDate = createdDate;
        this.goal = goal;
    }
}
