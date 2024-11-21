package com.example.goaltracker.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "progress")
@Data
@RequiredArgsConstructor
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(nullable = false)
    private double progressAmount;

    private String note;

    public Progress(Goal goal, Date timestamp, double progressAmount, String note) {
        this.goal = goal;
        this.timestamp = timestamp;
        this.progressAmount = progressAmount;
        this.note = note;
    }
}
