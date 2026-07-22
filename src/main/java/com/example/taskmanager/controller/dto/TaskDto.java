package com.example.taskmanager.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Details about the Task")
public class TaskDto {

    @Schema(description = "Unique identifier of the task", example = "1")
    private Long id;
    @Schema(description = "Task title", example = "Creation")
    private String title;
    @Schema(description = "Task description", example = "Creation REST-full application")
    private String description;
    @Schema(description = "Status: on of TODO, IN_PROGRESS, DONE", example = "DONE", nullable = true)
    private String status;
    @Schema(description = "Priority: on of LOW, MEDIUM, HIGH", example = "HIGH", nullable = true)
    private String priority;
    @Schema(description = "Author by Username of user", example = "Tom Cruz")
    private String author;
    @Schema(description = "Assignee by Username of user", example = "Sean Connery", nullable = true)
    private String assignee;
}
