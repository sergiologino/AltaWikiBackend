package com.example.wikibackend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue
    private UUID id = UUID.randomUUID(); // Генерация UUID на стороне Java

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    @Column(nullable = false)
    private String author;

    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Document parent;

    // Конструкторы, геттеры и сеттеры

    public Document() {
    }

    public Document(String title, DocumentStatus status, LocalDateTime createdAt, LocalDateTime lastModifiedAt, String author, Space space) {
        this.title = title;
        this.status = status;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.author = author;
        this.space = space;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public Document getParent() {
        return parent;
    }

    public void setParent(Document parent) {
        this.parent = parent;
    }
}

