package com.example.wikibackend.model.mongodb;

import com.example.wikibackend.model.DocumentStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "wiki_content")
public class WikiContent {

    @Id
    private String id;

    private UUID documentId; // ID документа из PostgreSQL
    private String content; // Содержимое документа
    private String version; // Версия документа (например, "v1.0", "v1.1")
    private DocumentStatus status; // Статус документа: DRAFT, ACTIVE, OUTDATED
    private LocalDateTime createdAt; // Дата создания версии
    private String author; // Автор версии
    private UUID userId; // ID пользователя, если это черновик

    // Конструкторы, геттеры и сеттеры

    public WikiContent() {}

    public WikiContent(UUID documentId, String content, String version, DocumentStatus status, LocalDateTime createdAt, String author, UUID userId) {
        this.documentId = documentId;
        this.content = content;
        this.version = version;
        this.status = status;
        this.createdAt = createdAt;
        this.author = author;
        this.userId = userId;
    }

    // Геттеры и сеттеры

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}


