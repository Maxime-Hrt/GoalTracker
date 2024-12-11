package com.example.goaltracker.services;

import com.example.goaltracker.dto.GoalDTO;
import com.example.goaltracker.dto.GoalResponseDTO;
import com.example.goaltracker.entities.Goal;
import com.example.goaltracker.entities.User;
import com.example.goaltracker.repositories.GoalRepository;
import com.example.goaltracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Autowired
    public GoalService(GoalRepository goalRepository, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    public GoalResponseDTO createGoal(GoalDTO goalDTO, Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOpt.get();
        Goal goal = new Goal(
                goalDTO.title(),
                goalDTO.description(),
                goalDTO.status(),
                LocalDateTime.now(), // createdDate is the current date
                goalDTO.targetDate(),
                user
        );

        goal.setColor(goalDTO.color()); // Optionnel, si la couleur est fournie

        Goal savedGoal = goalRepository.save(goal);
        return new GoalResponseDTO(
                savedGoal.getId(),
                savedGoal.getTitle(),
                savedGoal.getDescription(),
                savedGoal.getStatus(),
                savedGoal.getCreatedDate(),
                savedGoal.getTargetDate(),
                savedGoal.getColor()
        );
    }

    public List<GoalResponseDTO> getAllGoalsForUser(Long userId) {
        List<Goal> goals = goalRepository.findByUserId(userId);
        return goals.stream().map(goal -> new GoalResponseDTO(
                goal.getId(),
                goal.getTitle(),
                goal.getDescription(),
                goal.getStatus(),
                goal.getCreatedDate(),
                goal.getTargetDate(),
                goal.getColor()
        )).toList();
    }

    public GoalResponseDTO getGoalById(Long goalId, Long userId) {
        Goal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found"));
        return new GoalResponseDTO(
                goal.getId(),
                goal.getTitle(),
                goal.getDescription(),
                goal.getStatus(),
                goal.getCreatedDate(),
                goal.getTargetDate(),
                goal.getColor()
        );
    }

    public GoalResponseDTO updateGoal(Long goalId, GoalDTO goalDTO, Long userId) {
        Goal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found"));

        goal.setTitle(goalDTO.title());
        goal.setDescription(goalDTO.description());
        goal.setStatus(goalDTO.status());
        goal.setTargetDate(goalDTO.targetDate());
        goal.setColor(goalDTO.color());

        Goal updatedGoal = goalRepository.save(goal);
        return new GoalResponseDTO(
                updatedGoal.getId(),
                updatedGoal.getTitle(),
                updatedGoal.getDescription(),
                updatedGoal.getStatus(),
                updatedGoal.getCreatedDate(),
                updatedGoal.getTargetDate(),
                updatedGoal.getColor()
        );
    }

    public void deleteGoal(Long goalId, Long userId) {
        Goal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found"));
        goalRepository.delete(goal);
    }
}