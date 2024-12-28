package com.example.wikibackend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private UUID id; // Генерация UUID на стороне Java

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


    @Column(name = "space_id", nullable = false)
    private UUID spaceId;


    @Column(name = "parent_id")
    private UUID parent;

    // Добавляем связь с RoleDocumentAccess
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoleDocumentAccess> roleAccesses = new ArrayList<>();

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

    public UUID getSpaceId() {
        return spaceId;
    }

    public void setSpace(UUID spaceId) {
        this.spaceId = spaceId;
    }

    public UUID getParent() {
        return parent;
    }

    public void setParent(UUID parent) {
        this.parent = parent;
    }

    public List<RoleDocumentAccess> getRoleAccesses() {
        return roleAccesses;
    }

    public void setRoleAccesses(List<RoleDocumentAccess> roleAccesses) {
        this.roleAccesses = roleAccesses;
    }
}

