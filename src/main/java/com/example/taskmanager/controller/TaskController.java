package com.example.taskmanager.controller;

import com.example.taskmanager.controller.dto.TaskDto;
import com.example.taskmanager.enums.Status;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public TaskDto createTask(@RequestBody TaskDto dto) {
        return taskMapper.mapToTaskDto(
            taskService.save(taskMapper.mapToTask(dto))
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public TaskDto getTaskById(@PathVariable("id") Long taskId) {
        Task task = taskService.getTaskById(taskId);
        if (nonNull(task)) {
            return taskMapper.mapToTaskDto(task);
        }
        throw new IllegalStateException("Requested task not found");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<TaskDto> getTasks(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) Long authorId) {
        return taskMapper.mapToTaskDtoList(
            taskService.getAllTask(status, assigneeId, authorId)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') || @taskService.isAuthor(#taskId)")
    public TaskDto updateTask(@PathVariable("id") Long taskId, @RequestBody TaskDto dto) {
        Task task = taskService.update(taskId, taskMapper.mapToTask(dto));
        if (nonNull(task)) {
            return taskMapper.mapToTaskDto(task);
        }
        throw new IllegalStateException("Requested task not found");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') || @taskService.isAuthor(#taskId)")
    public void deleteTask(@PathVariable("id") Long taskId) {
        taskService.remove(taskId);
    }
}
