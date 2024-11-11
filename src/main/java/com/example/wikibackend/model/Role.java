package com.example.wikibackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue
    private UUID id = UUID.randomUUID();

    private String role_name;

    private String description;

    public UUID getId() {
        return id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }
    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Role(UUID id, String role_name, String description) {
        this.id = id;
        this.role_name = role_name;
        this.description = description;
    }

    public Role() {
    }
}
