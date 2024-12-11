package com.example.goaltracker.controllers;

import com.example.goaltracker.dto.GoalDTO;
import com.example.goaltracker.dto.GoalResponseDTO;
import com.example.goaltracker.services.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    @Autowired
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<GoalResponseDTO> createGoal(@RequestBody GoalDTO goalDTO, @PathVariable Long userId) {
        GoalResponseDTO goalResponseDTO = goalService.createGoal(goalDTO, userId);
        return new ResponseEntity<>(goalResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<GoalResponseDTO>> getAllGoalsForUser(@PathVariable Long userId) {
        List<GoalResponseDTO> goals = goalService.getAllGoalsForUser(userId);
        return new ResponseEntity<>(goals, HttpStatus.OK);
    }

    @GetMapping("/{userId}/{goalId}")
    public ResponseEntity<GoalResponseDTO> getGoalById(@PathVariable Long userId, @PathVariable Long goalId) {
        GoalResponseDTO goal = goalService.getGoalById(goalId, userId);
        return new ResponseEntity<>(goal, HttpStatus.OK);
    }

    @PutMapping("/{userId}/{goalId}")
    public ResponseEntity<GoalResponseDTO> updateGoal(
            @PathVariable Long userId, @PathVariable Long goalId, @RequestBody GoalDTO goalDTO) {
        GoalResponseDTO updatedGoal = goalService.updateGoal(goalId, goalDTO, userId);
        return new ResponseEntity<>(updatedGoal, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long userId, @PathVariable Long goalId) {
        goalService.deleteGoal(goalId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
