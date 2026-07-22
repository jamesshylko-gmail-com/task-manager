package com.example.taskmanager.mapper;

import com.example.taskmanager.controller.dto.TaskDto;
import com.example.taskmanager.enums.Priority;
import com.example.taskmanager.enums.Status;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.taskmanager.enums.Priority.LOW;
import static com.example.taskmanager.enums.Status.TODO;
import static java.util.Objects.isNull;

@Mapper(
        componentModel = "spring",
        imports = { Status.class, Priority.class }
)
public abstract class TaskMapper {

    @Autowired
    protected UserService userService;

    @Mapping(target = "status", source = "status", qualifiedByName = "toStatus")
    @Mapping(target = "priority", source = "priority", qualifiedByName = "toPriority")
    @Mapping(target = "author", expression = "java(userService.getCurrentUser())")
    @Mapping(target = "assignee", source = "assignee", qualifiedByName = "toUser")
    public abstract Task mapToTask(TaskDto dto);

    @Mapping(target = "author", source = "author.username")
    @Mapping(target = "assignee", source = "assignee", qualifiedByName = "getUserName")
    public abstract TaskDto mapToTaskDto(Task task);

    public abstract List<TaskDto> mapToTaskDtoList(List<Task> tasks);

    @Named("toStatus")
    protected Status toStatus(String status) {
        return isNull(status) ? TODO : Status.valueOf(status);
    }

    @Named("toPriority")
    protected Priority toPriority(String priority) {
        return isNull(priority) ? LOW : Priority.valueOf(priority);
    }

    @Named("toUser")
    protected User toUser(String username) {
        return isNull(username) ? null : userService.findByUsername(username);
    }

    @Named("getUserName")
    protected String getUserName(User user) {
        return isNull(user) ? null : user.getUsername();
    }
}
