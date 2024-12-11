package com.example.goaltracker.dto;

import com.example.goaltracker.entities.Color;
import com.example.goaltracker.entities.GoalStatus;

import java.time.LocalDateTime;

public record GoalDTO(
        String title,
        String description,
        GoalStatus status,
        LocalDateTime targetDate,
        Color color
) {
}
