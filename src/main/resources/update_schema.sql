-- Создание таблицы разделов
CREATE TABLE spaces (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        author VARCHAR(100) NOT NULL
);

-- Обновление таблицы документов
DROP TABLE IF EXISTS documents;

CREATE TABLE documents (
                           id SERIAL PRIMARY KEY,
                           title VARCHAR(255) NOT NULL,
                           status VARCHAR(50) NOT NULL,
                           created_at TIMESTAMP NOT NULL,
                           last_modified_at TIMESTAMP NOT NULL,
                           author VARCHAR(100) NOT NULL,
                           space_id INTEGER NOT NULL,
                           FOREIGN KEY (space_id) REFERENCES spaces(id)
);
