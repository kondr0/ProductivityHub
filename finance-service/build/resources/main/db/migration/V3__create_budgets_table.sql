CREATE TABLE finance.budgets (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    category_id UUID REFERENCES finance.categories(id) ON DELETE SET NULL,
    amount DECIMAL(12, 2) NOT NULL,
    period VARCHAR(16) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_budgets_user_id ON finance.budgets(user_id);
