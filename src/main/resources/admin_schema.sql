-- Создание схемы admin для хранения общих данных
CREATE SCHEMA IF NOT EXISTS admin;

-- Создание таблицы organizations в схеме admin
CREATE TABLE admin.organizations (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                     name VARCHAR(255) NOT NULL,
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);