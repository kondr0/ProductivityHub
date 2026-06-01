CREATE TABLE todo.task_tags (
    task_id UUID NOT NULL REFERENCES todo.tasks(id) ON DELETE CASCADE,
    tag_id UUID NOT NULL REFERENCES todo.tags(id) ON DELETE CASCADE,
    PRIMARY KEY (task_id, tag_id)
);
