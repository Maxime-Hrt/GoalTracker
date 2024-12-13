package com.example.goaltracker.dto;

import com.example.goaltracker.entities.Goal;

import java.util.List;

public record GoalAndTaskDTO(Goal goal, List<TaskDTO> tasks) {
}
