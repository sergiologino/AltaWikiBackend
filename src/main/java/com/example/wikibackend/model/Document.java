package com.example.wikibackend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue
    @Column(name = "id",nullable = false)
    private UUID id = UUID.randomUUID(); // Генерация UUID на стороне Java

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DocumentStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at",nullable = false)
    private LocalDateTime lastModifiedAt;

    @Column(name = "author_id",nullable = false)
    private UUID authorId;

    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private Space spaceId;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Document parent;

    // Конструкторы, геттеры и сеттеры

    public Document() {
    }

    public Document(String title, DocumentStatus status, LocalDateTime createdAt, LocalDateTime lastModifiedAt, UUID authorId, Space space) {
        this.title = title;
        this.status = status;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.authorId = authorId;
        this.spaceId = spaceId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public UUID getAuthor() {
        return authorId;
    }

    public void setAuthor(UUID author) {
        this.authorId = author;
    }

    public Space getSpaceId() {
        return spaceId;
    }

    public void setSpace(Space spaceId) {
        this.spaceId = spaceId;
    }

    public Document getParent() {
        return parent;
    }

    public void setParent(Document parent) {
        this.parent = parent;
    }
}

