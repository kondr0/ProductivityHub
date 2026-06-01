CREATE SCHEMA IF NOT EXISTS todo;

CREATE TABLE todo.tasks (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title VARCHAR(256) NOT NULL,
    description TEXT,
    status VARCHAR(16) NOT NULL DEFAULT 'TODO',
    priority VARCHAR(16) NOT NULL DEFAULT 'MEDIUM',
    due_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_tasks_user_id ON todo.tasks(user_id);
CREATE INDEX idx_tasks_status ON todo.tasks(status);
CREATE INDEX idx_tasks_due_date ON todo.tasks(due_date);
