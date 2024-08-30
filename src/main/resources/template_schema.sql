-- Создание шаблонной схемы
CREATE SCHEMA template_schema;

-- Создание таблиц в шаблонной схеме
CREATE TABLE template_schema.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE template_schema.roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE template_schema.spaces (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    author VARCHAR(100) NOT NULL
);

CREATE TABLE template_schema.documents (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    last_modified_at TIMESTAMP NOT NULL,
    author VARCHAR(100) NOT NULL,
    space_id INTEGER NOT NULL,
    parent_id INTEGER,
    FOREIGN KEY (space_id) REFERENCES template_schema.spaces(id),
    FOREIGN KEY (parent_id) REFERENCES template_schema.documents(id)
);


CREATE TABLE template_schema.user_space_access (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    space_id INTEGER NOT NULL,
    access_type VARCHAR(20) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES template_schema.users(id),
    CONSTRAINT fk_space FOREIGN KEY(space_id) REFERENCES template_schema.spaces(id)
);
