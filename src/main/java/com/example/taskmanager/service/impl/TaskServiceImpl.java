package com.example.taskmanager.service.impl;

import com.example.taskmanager.enums.Status;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.perository.TaskRepository;
import com.example.taskmanager.perository.specification.TaskSpecification;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.nonNull;

@Service("taskService")
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Override
    public List<Task> getAllTask(Status status, Long assigneeId, Long authorId) {
        Specification<Task> spec = TaskSpecification.hasStatus(status)
                .and(TaskSpecification.hasAuthor(authorId))
                .and(TaskSpecification.hasAssignee(assigneeId));
        return taskRepository.findAll(spec);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findTaskById(id)
                .orElseThrow(() -> new IllegalStateException("Task with given id not found"));
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task update(Long taskId, Task task) {
        Task existingTask = taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new IllegalStateException("Task with given id not found"));
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setPriority(task.getPriority());
        existingTask.setAssignee(task.getAssignee());
        return taskRepository.save(existingTask);
    }

    @Override
    public void remove(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public boolean isAuthor(Long taskId) {
        User user = userService.getCurrentUser();
        if (nonNull(user)) {
            Task task = taskRepository.findTaskById(taskId)
                    .orElseThrow(() -> new IllegalStateException("Task with given id not found"));
            return task.getAuthor().getUsername().equals(user.getUsername());
        }
        return false;
    }
}
