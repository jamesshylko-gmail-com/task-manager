package com.example.taskmanager.controller.dto;

import lombok.Data;

@Data
public class TaskDto {

    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String author;
    private String assignee;
}
