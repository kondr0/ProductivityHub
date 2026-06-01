CREATE SCHEMA IF NOT EXISTS auth;

CREATE TABLE auth.users (
    id UUID PRIMARY KEY,
    email VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    first_name VARCHAR(64),
    last_name VARCHAR(64),
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_users_email ON auth.users(email);
