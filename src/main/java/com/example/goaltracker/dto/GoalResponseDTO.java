package com.example.goaltracker.dto;

import com.example.goaltracker.entities.Color;
import com.example.goaltracker.entities.GoalStatus;

import java.time.LocalDateTime;

public record GoalResponseDTO(
        Long id,
        String title,
        String description,
        GoalStatus status,
        LocalDateTime createdDate,
        LocalDateTime targetDate,
        Color color
) {
}
