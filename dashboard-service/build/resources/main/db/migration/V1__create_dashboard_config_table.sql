CREATE SCHEMA IF NOT EXISTS dashboard;

CREATE TABLE dashboard.dashboard_config (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    layout TEXT,
    widgets_json TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_dashboard_config_user_id ON dashboard.dashboard_config(user_id);
