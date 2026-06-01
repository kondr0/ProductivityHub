CREATE SCHEMA IF NOT EXISTS module_registry;

CREATE TABLE module_registry.modules (
    id UUID PRIMARY KEY,
    code VARCHAR(64) NOT NULL UNIQUE,
    name VARCHAR(128) NOT NULL,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    service_url VARCHAR(256),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO module_registry.modules (id, code, name, description) VALUES
    ('a0000000-0000-0000-0000-000000000001', 'todo', 'Задачи', 'Управление задачами и списками дел'),
    ('a0000000-0000-0000-0000-000000000002', 'finance', 'Финансы', 'Учет доходов и расходов'),
    ('a0000000-0000-0000-0000-000000000003', 'notes', 'Заметки', 'Заметки с поддержкой Markdown'),
    ('a0000000-0000-0000-0000-000000000004', 'planner', 'Планировщик', 'Календарь и планирование событий'),
    ('a0000000-0000-0000-0000-000000000005', 'dashboard', 'Дашборд', 'Панель метрик и виджетов');

CREATE TABLE module_registry.user_modules (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    module_id UUID NOT NULL REFERENCES module_registry.modules(id) ON DELETE CASCADE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    UNIQUE (user_id, module_id)
);

CREATE INDEX idx_user_modules_user_id ON module_registry.user_modules(user_id);
