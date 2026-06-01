CREATE SCHEMA IF NOT EXISTS finance;

CREATE TABLE finance.transactions (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    type VARCHAR(16) NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    description VARCHAR(256),
    transaction_date DATE NOT NULL,
    category_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_transactions_user_id ON finance.transactions(user_id);
CREATE INDEX idx_transactions_date ON finance.transactions(transaction_date);
CREATE INDEX idx_transactions_type ON finance.transactions(type);
