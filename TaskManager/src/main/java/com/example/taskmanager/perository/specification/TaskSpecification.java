package com.example.taskmanager.perository.specification;

import com.example.taskmanager.enums.Status;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import static java.util.Objects.isNull;

public class TaskSpecification {

    public static Specification<Task> hasStatus(Status status) {
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Task> hasAuthor(Long authorId) {
        return (root, query, criteriaBuilder) ->
                userPredicate(root, criteriaBuilder, "author", authorId);
    }

    public static Specification<Task> hasAssignee(Long assigneeId) {
        return (root, query, criteriaBuilder) ->
                userPredicate(root, criteriaBuilder, "assignee", assigneeId);
    }

    private static Predicate userPredicate(Root<Task> root, CriteriaBuilder criteriaBuilder, String fieldName, Long userId) {
        if (isNull(userId)) {
            return criteriaBuilder.conjunction(); // Returns an empty valid predicate (1=1)
        }
        Join<Task, User> departmentJoin = root.join(fieldName);
        return criteriaBuilder.equal(departmentJoin.get("id"), userId);
    }
}
