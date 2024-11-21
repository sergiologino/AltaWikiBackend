package com.example.wikibackend.model;

import jakarta.persistence.*;

import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "organizations", schema = "admin")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Идентификатор организации (UUID)

    private String name; // Название организации

//    @GeneratedValue(strategy = GenerationType.AUTO,generator = "my_seq")
//    @SequenceGenerator(name = "my_seq", sequenceName = "my_seq", allocationSize = 1)
    private Long alias; // Алиас организации (долгий идентификатор)

    // Геттеры и сеттеры для всех полей

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
