CREATE SCHEMA IF NOT EXISTS notes;

CREATE TABLE notes.notes (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title VARCHAR(256) NOT NULL,
    content TEXT,
    is_pinned BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_notes_user_id ON notes.notes(user_id);

CREATE TABLE notes.note_tags (
    id UUID PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    user_id UUID NOT NULL,
    color VARCHAR(16)
);

CREATE INDEX idx_note_tags_user_id ON notes.note_tags(user_id);

CREATE TABLE notes.note_note_tags (
    note_id UUID NOT NULL REFERENCES notes.notes(id) ON DELETE CASCADE,
    tag_id UUID NOT NULL REFERENCES notes.note_tags(id) ON DELETE CASCADE,
    PRIMARY KEY (note_id, tag_id)
);
