package com.example.wikibackend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "spaces")
public class Space {

    @Id
    @GeneratedValue
    private UUID id = UUID.randomUUID();
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String author;

    // Конструкторы, геттеры и сеттеры

    public Space() {
    }

    public Space(String name, LocalDateTime createdAt, String author) {
        this.name = name;
        this.createdAt = createdAt;
        this.author = author;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}

