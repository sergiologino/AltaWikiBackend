-- Создание расширения для генерации UUID
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
-- Создание схемы template_schema
CREATE SCHEMA IF NOT EXISTS template_schema;

-- Таблица пользователей
CREATE TABLE template_schema.users (
                                        id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                                        username varchar(255) UNIQUE NOT NULL,
                                        email varchar(255) UNIQUE NOT NULL,
                                        password varchar(255) NOT NULL,
                                        enabled bool NOT NULL,
                                        deleted bool NOT NULL
);

-- Таблица ролей
CREATE TABLE roles (
                                        id UUID PRIMARY KEY, -- Идентификатор роли
                                        role_name VARCHAR(255) NOT NULL, -- Название роли
                                        description TEXT, -- Описание роли
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Дата создания
                                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Дата обновления
);

-- Таблица связей пользователей и ролей
CREATE TABLE template_schema.user_roles (
                                        user_id uuid NOT NULL REFERENCES template_schema.users(id) ON DELETE CASCADE,
                                        role_id uuid NOT NULL REFERENCES template_schema.roles(id) ON DELETE CASCADE,
                                        PRIMARY KEY (user_id, role_id)
);

-- Таблица разделов
CREATE TABLE template_schema.spaces (
                                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                          name VARCHAR(255) NOT NULL,
                                          description VARCHAR(255),
                                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          author_id UUID NOT NULL,
                                          CONSTRAINT fk_space_author FOREIGN KEY(author_id) REFERENCES template_schema.users(id)
);

-- Таблица документов
CREATE TABLE template_schema.documents (
                                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                           title VARCHAR(255) NOT NULL,
                                           status VARCHAR(50) NOT NULL,
                                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           last_modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           author_id UUID NOT NULL,
                                           space_id UUID,
                                           parent_id UUID,
                                           CONSTRAINT fk_author FOREIGN KEY(author_id) REFERENCES template_schema.users(id),
                                           CONSTRAINT fk_section FOREIGN KEY(space_id) REFERENCES template_schema.spaces(id),
                                           CONSTRAINT fk_parent FOREIGN KEY(parent_id) REFERENCES template_schema.documents(id)
);

-- Таблица доступа к пространствам (user_space_access)
CREATE TABLE template_schema.user_space_access (
                                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                           user_id UUID NOT NULL REFERENCES template_schema.users(id) ON DELETE CASCADE,
                                           space_id UUID NOT NULL REFERENCES template_schema.spaces(id) ON DELETE CASCADE,
                                           access_type VARCHAR(50) NOT NULL CHECK (access_type IN ('READ', 'WRITE', 'ADMIN'))
);

CREATE TABLE comments (
                                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Уникальный идентификатор комментария
                                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Дата создания комментария
                                            updated_at TIMESTAMP, -- Дата последнего изменения
                                            is_resolved BOOLEAN NOT NULL DEFAULT FALSE, -- Признак "решён/не решён"
                                            anchor TEXT, -- Якорь, привязка к месту в документе
                                            author_id UUID NOT NULL, -- ID автора комментария
                                            parent_id UUID REFERENCES comments(id) ON DELETE CASCADE, -- ID родительского комментария
                                            document_id UUID NOT NULL, -- ID документа, к которому относится комментарий
                                            document_version TEXT NOT NULL, -- Версия документа
                                            content TEXT NOT NULL, -- Текст комментария

    CONSTRAINT fk_document FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE -- Связь с таблицей документов
);
CREATE TABLE role_document_access (
                                            id UUID PRIMARY KEY,
                                            role_id UUID NOT NULL,
                                            document_id UUID NOT NULL,
                                            access_type VARCHAR(10) NOT NULL,
                                            FOREIGN KEY (role_id) REFERENCES roles(id),
                                            FOREIGN KEY (document_id) REFERENCES documents(id)
);


-- Приведение ключевых полей к UUID
ALTER TABLE template_schema.documents
    ALTER COLUMN id SET DATA TYPE UUID USING id::uuid,
    ALTER COLUMN parent_id SET DATA TYPE UUID USING parent_id::uuid,
    ALTER COLUMN space_id SET DATA TYPE UUID USING space_id::uuid,
    ALTER COLUMN author_id SET DATA TYPE UUID USING author_id::uuid;

ALTER TABLE template_schema.user_roles
    ALTER COLUMN user_id SET DATA TYPE UUID USING user_id::uuid,
    ALTER COLUMN role_id SET DATA TYPE UUID USING user_id::uuid;

ALTER TABLE template_schema.users
    ALTER COLUMN id SET DATA TYPE UUID USING id::uuid;

ALTER TABLE template_schema.roles
    ALTER COLUMN id SET DATA TYPE UUID USING id::uuid;

ALTER TABLE template_schema.spaces
    ALTER COLUMN id SET DATA TYPE UUID USING id::uuid,
    ALTER COLUMN author_id SET DATA TYPE UUID USING author_id::uuid;

ALTER TABLE template_schema.user_space_access
    ALTER COLUMN id SET DATA TYPE UUID USING id::uuid,
    ALTER COLUMN user_id SET DATA TYPE UUID USING user_id::uuid,
    ALTER COLUMN space_id SET DATA TYPE UUID USING space_id::uuid;

ALTER TABLE template_schema.comments
    ALTER COLUMN id SET DATA TYPE UUID USING id::uuid,
    ALTER COLUMN author_id SET DATA TYPE UUID USING author_id::uuid,
    ALTER COLUMN parent_id SET DATA TYPE UUID USING parent_id::uuid;
    ALTER COLUMN document_id SET DATA TYPE UUID USING document_id::uuid;



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

