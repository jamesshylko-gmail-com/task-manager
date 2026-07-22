package com.example.taskmanager.controller;

import com.example.taskmanager.controller.dto.TaskDto;
import com.example.taskmanager.enums.Status;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task management", description = "APIs for managing task operation")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Operation(
            summary = "Creation",
            description = "Create task"
    )
    @ApiResponse(responseCode = "200", description = "Task created")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public TaskDto createTask(@RequestBody TaskDto dto) {
        return taskMapper.mapToTaskDto(
            taskService.save(taskMapper.mapToTask(dto))
        );
    }

    @Operation(
            summary = "Task by id",
            description = "Receive task information by id"
    )
    @ApiResponse(responseCode = "200", description = "Task received")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public TaskDto getTaskById(@PathVariable("id") Long taskId) {
        Task task = taskService.getTaskById(taskId);
        if (nonNull(task)) {
            return taskMapper.mapToTaskDto(task);
        }
        throw new IllegalStateException("Requested task not found");
    }

    @Operation(
            summary = "Get Tasks",
            description = "Get all tasks by filters"
    )
    @ApiResponse(responseCode = "200", description = "Tasks received")
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

    @Operation(
            summary = "Update",
            description = "Update task information by id"
    )
    @ApiResponse(responseCode = "200", description = "Task updated")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') || @taskService.isAuthor(#taskId)")
    public TaskDto updateTask(@PathVariable("id") Long taskId, @RequestBody TaskDto dto) {
        Task task = taskService.update(taskId, taskMapper.mapToTask(dto));
        if (nonNull(task)) {
            return taskMapper.mapToTaskDto(task);
        }
        throw new IllegalStateException("Requested task not found");
    }

    @Operation(
            summary = "Delete",
            description = "Delete task by id"
    )
    @ApiResponse(responseCode = "200", description = "Task deleted")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') || @taskService.isAuthor(#taskId)")
    public void deleteTask(@PathVariable("id") Long taskId) {
        taskService.remove(taskId);
    }
}
