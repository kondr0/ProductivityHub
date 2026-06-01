CREATE TABLE todo.tags (
    id UUID PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    user_id UUID NOT NULL,
    color VARCHAR(16)
);

CREATE INDEX idx_tags_user_id ON todo.tags(user_id);
CREATE UNIQUE INDEX idx_tags_name_user ON todo.tags(name, user_id);
