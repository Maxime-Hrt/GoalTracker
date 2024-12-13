package com.example.goaltracker.services;

import com.example.goaltracker.dto.GoalAndTaskDTO;
import com.example.goaltracker.dto.TaskDTO;
import com.example.goaltracker.entities.Goal;
import com.example.goaltracker.entities.Task;
import com.example.goaltracker.repositories.GoalRepository;
import com.example.goaltracker.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final GoalRepository goalRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, GoalRepository goalRepository) {
        this.taskRepository = taskRepository;
        this.goalRepository = goalRepository;
    }

    public Task createTask(Long goalId, TaskDTO taskDTO) {
        Optional<Goal> goalOptional = goalRepository.findById(goalId);
        if (goalOptional.isEmpty()) {
            throw new IllegalArgumentException("Goal not found with ID: " + goalId);
        }

        Goal goal = goalOptional.get();
        Task task = new Task(
                taskDTO.title(),
                taskDTO.isCompleted(),
                LocalDateTime.now(),
                goal
        );
        return taskRepository.save(task);
    }

    public List<Task> getTasksByGoalId(Long goalId) {
        return taskRepository.findByGoalId(goalId);
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public List<Task> getTasksByUserId(Long userId) {
        List<Goal> goals = goalRepository.findByUserId(userId);
        return taskRepository.findByGoalIdIn(goals.stream().map(Goal::getId).toList());
    }

    public Task updateTask(Long taskId, TaskDTO taskDTO) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new IllegalArgumentException("Task not found with ID: " + taskId);
        }

        Task task = taskOptional.get();
        task.setTitle(taskDTO.title());
        task.setCompleted(taskDTO.isCompleted());

        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
