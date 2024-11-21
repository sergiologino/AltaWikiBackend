-- Создание схемы admin для хранения общих данных
CREATE SCHEMA IF NOT EXISTS admin;

-- Создание таблицы organizations в схеме admin
CREATE TABLE admin.organizations (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                     name VARCHAR(255) NOT NULL,
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                                     alias INT8 NOT NULL AUTO_INCREMENT
);
CREATE TABLE IF NOT EXISTS admin.users_organization (
                                                 user_id UUID PRIMARY KEY,
                                                 organization_id UUID NOT NULL REFERENCES admin.organizations(id),
                                                 username VARCHAR(255) NOT NULL
);
-- Создание таблицы user_organization в схеме admin
CREATE TABLE admin.user_organization (
    user_id UUID PRIMARY KEY,
    organization_id UUID NOT NULL,
    username VARCHAR(100) NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES admin.organizations(id)
);
