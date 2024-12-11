package com.example.goaltracker.controllers;

import com.example.goaltracker.dto.TaskDTO;
import com.example.goaltracker.dto.TaskResponseDTO;
import com.example.goaltracker.entities.Task;
import com.example.goaltracker.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/{goalId}")
    public ResponseEntity<TaskResponseDTO> createTask(@PathVariable Long goalId, @RequestBody TaskDTO taskDTO) {
        Task task = taskService.createTask(goalId, taskDTO);
        TaskResponseDTO response = new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.isCompleted(),
                task.getCreatedDate(),
                task.getGoal().getId()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByGoalId(@PathVariable Long goalId) {
        List<TaskResponseDTO> tasks = taskService.getTasksByGoalId(goalId).stream()
                .map(task -> new TaskResponseDTO(
                        task.getId(),
                        task.getTitle(),
                        task.isCompleted(),
                        task.getCreatedDate(),
                        task.getGoal().getId()
                ))
                .collect(Collectors.toList());
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long taskId) {
        Optional<Task> taskOptional = taskService.getTaskById(taskId);
        if (taskOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Task task = taskOptional.get();
        TaskResponseDTO response = new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.isCompleted(),
                task.getCreatedDate(),
                task.getGoal().getId()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long taskId, @RequestBody TaskDTO taskDTO) {
        Task task = taskService.updateTask(taskId, taskDTO);
        TaskResponseDTO response = new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.isCompleted(),
                task.getCreatedDate(),
                task.getGoal().getId()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
