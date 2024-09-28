package com.example.wikibackend.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user_organization", schema = "admin")
public class UserAdmin {

    @Id
    @GeneratedValue
    private UUID id = UUID.randomUUID();
    // text

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private  UUID organizationId;


    public UserAdmin() {
    }

    public UserAdmin(String username, UUID organizationId) {
        this.username = username;

    }

    // Геттеры и сеттеры

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setOrganizationId(UUID organizationId) { this.organizationId=organizationId;}

    public UUID getOrganizationId() { return organizationId;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}