-- Создание расширения для генерации UUID
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
-- Создание схемы template_schema
CREATE SCHEMA IF NOT EXISTS template_schema;

-- Таблица пользователей
CREATE TABLE template_schema.users (
                                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                       username VARCHAR(255) UNIQUE NOT NULL,
                                       email VARCHAR(255) UNIQUE NOT NULL,
                                       password VARCHAR(255) NOT NULL,
                                       enabled BOOLEAN NOT NULL DEFAULT TRUE,
                                       deleted BOOLEAN NOT NULL DEFAULT FALSE
);

-- Таблица ролей
CREATE TABLE template_schema.roles (
                                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                       role_name VARCHAR(255) UNIQUE NOT NULL
);

-- Таблица связей пользователей и ролей
CREATE TABLE template_schema.user_roles (
                                            user_id INT NOT NULL,
                                            role_id INT NOT NULL,
                                            CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES template_schema.users(id),
                                            CONSTRAINT fk_role FOREIGN KEY(role_id) REFERENCES template_schema.roles(id),
                                            PRIMARY KEY (user_id, role_id)
);

-- Таблица разделов
CREATE TABLE template_schema.spaces (
                                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                          name VARCHAR(255) NOT NULL,
                                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          author_id INT NOT NULL,
                                          CONSTRAINT fk_space_author FOREIGN KEY(author_id) REFERENCES template_schema.users(id)
);

-- Таблица документов
CREATE TABLE template_schema.documents (
                                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                           title VARCHAR(255) NOT NULL,
                                           status VARCHAR(50) NOT NULL,
                                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           last_modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           author_id INT NOT NULL,
                                           section_id INT,
                                           parent_id INT,
                                           CONSTRAINT fk_author FOREIGN KEY(author_id) REFERENCES template_schema.users(id),
                                           CONSTRAINT fk_section FOREIGN KEY(section_id) REFERENCES template_schema.spaces(id),
                                           CONSTRAINT fk_parent FOREIGN KEY(parent_id) REFERENCES template_schema.documents(id)
);

-- Таблица доступа к пространствам (user_space_access)
CREATE TABLE template_schema.user_space_access (
                                                   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                                   user_id UUID NOT NULL REFERENCES template_schema.users(id) ON DELETE CASCADE,
                                                   space_id UUID NOT NULL REFERENCES template_schema.spaces(id) ON DELETE CASCADE,
                                                   access_type VARCHAR(50) NOT NULL CHECK (access_type IN ('READ', 'WRITE', 'ADMIN'))
);




-- Индексы для таблицы пользователей
CREATE INDEX idx_users_username ON template_schema.users(username);
CREATE INDEX idx_users_email ON template_schema.users(email);

-- Индексы для таблицы ролей
CREATE INDEX idx_roles_role_name ON template_schema.roles(role_name);

-- Индексы для таблицы документов
CREATE INDEX idx_documents_title ON template_schema.documents(title);
CREATE INDEX idx_documents_status ON template_schema.documents(status);
CREATE INDEX idx_documents_author_id ON template_schema.documents(author_id);

-- Индексы для таблицы разделов
CREATE INDEX idx_sections_name ON template_schema.spaces(name);
CREATE INDEX idx_sections_author_id ON template_schema.spaces(author_id);

-- Индексы для таблицы связей пользователей и ролей
CREATE INDEX idx_user_roles_user_id ON template_schema.user_roles(user_id);
CREATE INDEX idx_user_roles_role_id ON template_schema.user_roles(role_id);

-- Индексы для таблицы user_space_access

-- Индекс для быстрого поиска по user_id
CREATE INDEX idx_user_space_access_user_id
    ON template_schema.user_space_access (user_id);

-- Индекс для быстрого поиска по space_id
CREATE INDEX idx_user_space_access_space_id
    ON template_schema.user_space_access (space_id);

-- Композитный индекс для ускорения поиска по комбинации user_id и space_id
CREATE INDEX idx_user_space_access_user_space
    ON template_schema.user_space_access (user_id, space_id);

-- Индекс для быстрого поиска по access_type (если запросы по этому полю будут частыми)
CREATE INDEX idx_user_space_access_type
    ON template_schema.user_space_access (access_type);

-- Уникальные ключи для таблицы пользователей и ролей
ALTER TABLE template_schema.users ADD CONSTRAINT unique_username UNIQUE (username);
ALTER TABLE template_schema.users ADD CONSTRAINT unique_email UNIQUE (email);
ALTER TABLE template_schema.roles ADD CONSTRAINT unique_role_name UNIQUE (role_name);

-- Триггеры или другие необходимые объекты можно добавить здесь
