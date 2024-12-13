package com.example.goaltracker.repositories;

import com.example.goaltracker.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByGoalId(Long goalId);
    List<Task> findByGoalIdIn(List<Long> goalIds);
}
