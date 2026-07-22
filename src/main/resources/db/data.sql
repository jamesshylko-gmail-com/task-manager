INSERT INTO users (user_id, username, email, password, role)
VALUES (1, 'user', 'smith@gnmail.com', '$2a$10$r81HgWaGmz3XuHeuEpprgOjHBO.77bck4ySvGqwzmU.jlhKPmz5He', 'USER');
INSERT INTO users (user_id, username, email, password, role)
VALUES (2, 'admin', 'connery@gnmail.com', '$2a$10$cOeMBAWBCm/VM9EJgWLKl.KJXlvZjbMvgvzXb6XYxeLkY.XXS3cqu', 'ADMIN');

INSERT INTO tasks (task_id, title, description, status, priority, author_id, assignee_id, created, updated)
VALUES (1, 'Creation', 'Create controller for Rest-full TaskManager service', 'DONE', 'HIGH', 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tasks (task_id, title, description, status, priority, author_id, assignee_id, created, updated)
VALUES (2, 'Design', 'Add filters', 'TODO', 'MEDIUM', 2, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tasks (task_id, title, description, status, priority, author_id, assignee_id, created, updated)
VALUES (3, 'Publication', 'Publish to Git', 'TODO', 'LOW', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO tasks (task_id, title, description, status, priority, author_id, assignee_id, created, updated)
VALUES (4, 'Testing', 'Add tests', 'TODO', 'LOW', 2, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

ALTER TABLE users ALTER COLUMN user_id RESTART WITH 3;
ALTER TABLE tasks ALTER COLUMN task_id RESTART WITH 5;