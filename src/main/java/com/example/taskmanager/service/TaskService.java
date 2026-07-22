package com.example.taskmanager.service;

import com.example.taskmanager.enums.Status;
import com.example.taskmanager.model.Task;

import java.util.List;

public interface TaskService {

    List<Task> getAllTask(Status status, Long assignedId, Long authorId);

    Task getTaskById(Long id);

    boolean isAuthor(Long id);

    Task save(Task task);

    Task update(Long taskId, Task task);

    void remove(Long taskId);
}
