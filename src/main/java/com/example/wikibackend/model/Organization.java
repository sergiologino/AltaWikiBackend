package com.example.wikibackend.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "organizations", schema = "admin")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "alias", nullable = false)
    private Long alias;
        // Другие поля

    // Конструкторы
    public Organization() {
    }

    public Organization(String name) {
        this.name = name;

    }

    // Геттеры и сеттеры
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

    public Long getAlias() {
        return alias;
    }

    public void setAlias(Long alias) {
        this.alias = alias;
    }

}

