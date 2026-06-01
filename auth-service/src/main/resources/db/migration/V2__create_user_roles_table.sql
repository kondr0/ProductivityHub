CREATE TABLE auth.roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE
);

INSERT INTO auth.roles(name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');

CREATE TABLE auth.user_roles (
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES auth.roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);
