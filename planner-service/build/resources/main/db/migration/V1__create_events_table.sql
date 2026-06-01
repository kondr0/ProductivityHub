CREATE SCHEMA IF NOT EXISTS planner;

CREATE TABLE planner.events (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title VARCHAR(256) NOT NULL,
    description TEXT,
    event_date DATE NOT NULL,
    start_time TIME,
    end_time TIME,
    location VARCHAR(256),
    reminder_minutes_before INTEGER,
    color VARCHAR(16),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_events_user_id ON planner.events(user_id);
CREATE INDEX idx_events_date ON planner.events(event_date);
