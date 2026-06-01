CREATE TABLE finance.categories (
    id UUID PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    color VARCHAR(16),
    icon VARCHAR(64),
    user_id UUID NOT NULL,
    type VARCHAR(16) NOT NULL
);

CREATE INDEX idx_categories_user_id ON finance.categories(user_id);

INSERT INTO finance.categories (id, name, color, icon, user_id, type) VALUES
    ('00000000-0000-0000-0000-000000000001', 'Зарплата', '#4CAF50', 'salary', '00000000-0000-0000-0000-000000000000', 'INCOME'),
    ('00000000-0000-0000-0000-000000000002', 'Фриланс', '#2196F3', 'freelance', '00000000-0000-0000-0000-000000000000', 'INCOME'),
    ('00000000-0000-0000-0000-000000000003', 'Продукты', '#FF5722', 'food', '00000000-0000-0000-0000-000000000000', 'EXPENSE'),
    ('00000000-0000-0000-0000-000000000004', 'Транспорт', '#9C27B0', 'transport', '00000000-0000-0000-0000-000000000000', 'EXPENSE'),
    ('00000000-0000-0000-0000-000000000005', 'Развлечения', '#FF9800', 'entertainment', '00000000-0000-0000-0000-000000000000', 'EXPENSE');
