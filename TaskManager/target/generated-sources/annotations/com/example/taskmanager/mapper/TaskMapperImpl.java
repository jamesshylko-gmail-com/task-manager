package com.example.taskmanager.mapper;

import com.example.taskmanager.controller.dto.TaskDto;
import com.example.taskmanager.enums.Priority;
import com.example.taskmanager.enums.Status;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-22T14:27:26+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.11 (Microsoft)"
)
@Component
public class TaskMapperImpl extends TaskMapper {

    @Override
    public Task mapToTask(TaskDto dto) {
        if ( dto == null ) {
            return null;
        }

        Task.TaskBuilder task = Task.builder();

        task.status( toStatus( dto.getStatus() ) );
        task.priority( toPriority( dto.getPriority() ) );
        task.assignee( toUser( dto.getAssignee() ) );
        task.id( dto.getId() );
        task.title( dto.getTitle() );
        task.description( dto.getDescription() );

        task.author( userService.getCurrentUser() );

        return task.build();
    }

    @Override
    public TaskDto mapToTaskDto(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskDto taskDto = new TaskDto();

        taskDto.setAuthor( taskAuthorUsername( task ) );
        taskDto.setAssignee( getUserName( task.getAssignee() ) );
        taskDto.setId( task.getId() );
        taskDto.setTitle( task.getTitle() );
        taskDto.setDescription( task.getDescription() );
        if ( task.getStatus() != null ) {
            taskDto.setStatus( task.getStatus().name() );
        }
        if ( task.getPriority() != null ) {
            taskDto.setPriority( task.getPriority().name() );
        }

        return taskDto;
    }

    @Override
    public List<TaskDto> mapToTaskDtoList(List<Task> tasks) {
        if ( tasks == null ) {
            return null;
        }

        List<TaskDto> list = new ArrayList<TaskDto>( tasks.size() );
        for ( Task task : tasks ) {
            list.add( mapToTaskDto( task ) );
        }

        return list;
    }

    private String taskAuthorUsername(Task task) {
        User author = task.getAuthor();
        if ( author == null ) {
            return null;
        }
        return author.getUsername();
    }
}
