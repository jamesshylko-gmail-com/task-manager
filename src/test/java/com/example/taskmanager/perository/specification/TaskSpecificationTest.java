package com.example.taskmanager.perository.specification;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.perository.TaskRepository;
import com.example.taskmanager.perository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static com.example.taskmanager.enums.Priority.*;
import static com.example.taskmanager.enums.Role.ADMIN;
import static com.example.taskmanager.enums.Role.USER;
import static com.example.taskmanager.enums.Status.*;
import static java.time.ZonedDateTime.now;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class TaskSpecificationTest {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        User user1 = new User(null, "user1", "user1.gmail.com", "password1", ADMIN);
        User user2 = new User(null, "user2", "user2.gmail.com", "password2", USER);

        userRepository.saveAll(List.of(user1, user2));

        Task task1 = new Task(null, "Task1", "Task1 description", TODO, LOW, user1, user1, now(), now());
        Task task2 = new Task(null, "Task2", "Task2 description", TODO, MEDIUM, user1, user2, now(), now());
        Task task3 = new Task(null, "Task3", "Task3 description", IN_PROGRESS, LOW, user1, null, now(), now());
        Task task4 = new Task(null, "Task4", "Task4 description", IN_PROGRESS, HIGH, user2, user1, now(), now());
        Task task5 = new Task(null, "Task5", "Task5 description", DONE, MEDIUM, user2, user2, now(), now());
        Task task6 = new Task(null, "Task6", "Task6 description", DONE, HIGH, user2, null, now(), now());

        taskRepository.saveAll(List.of(task1, task2, task3, task4, task5, task6));
    }

    @Test
    void hasStatusTest() {
        // Given
        Specification<Task> spec = TaskSpecification.hasStatus(DONE);

        // When
        List<Task> result = taskRepository.findAll(spec);

        // Then
        assertThat(result).hasSize(2)
                .extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Task5", "Task6");
    }

    @Test
    void hasAuthorTest() {
        // Given
        User author = userRepository.findByUsername("user1");
        Specification<Task> spec = TaskSpecification.hasAuthor(author.getId());

        // When
        List<Task> result = taskRepository.findAll(spec);

        // Then
        assertThat(result).hasSize(3)
                .extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Task1", "Task2", "Task3");
    }

    @Test
    void hasAssigneeTest() {
        // Given
        User assignee = userRepository.findByUsername("user2");
        Specification<Task> spec = TaskSpecification.hasAssignee(assignee.getId());

        // When
        List<Task> result = taskRepository.findAll(spec);

        // Then
        assertThat(result).hasSize(2)
                .extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Task2", "Task5");
    }

    @Test
    void combineConditionTest() {
        // Given
        User author = userRepository.findByUsername("user2");
        User assignee = userRepository.findByUsername("user1");
        Specification<Task> spec = TaskSpecification.hasStatus(IN_PROGRESS)
                .and(TaskSpecification.hasAuthor(author.getId()))
                .and(TaskSpecification.hasAssignee(assignee.getId()));

        // When
        List<Task> result = taskRepository.findAll(spec);

        // Then
        assertThat(result).hasSize(1)
                .extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Task4");
    }

}