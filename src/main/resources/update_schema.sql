-- Создание таблицы разделов
CREATE TABLE spaces (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        author VARCHAR(100) NOT NULL
);

-- Обновление таблицы документов
DROP TABLE IF EXISTS documents;


