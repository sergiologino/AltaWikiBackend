-- Создание схемы admin для хранения общих данных
CREATE SCHEMA IF NOT EXISTS admin;

-- Создание таблицы organizations в схеме admin
CREATE TABLE admin.organizations (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                     name VARCHAR(255) NOT NULL,
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS admin.users_admin (
                                                 user_id UUID PRIMARY KEY,
                                                 organization_id UUID NOT NULL REFERENCES admin.organizations(id),
                                                 username VARCHAR(255) NOT NULL,
                                                 email VARCHAR(255) NOT NULL UNIQUE,
                                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
