package com.example.goaltracker.dto;

import java.time.LocalDateTime;

public record TaskResponseDTO(Long id, String title, boolean isCompleted, LocalDateTime createdDate, Long goalId) {
}
