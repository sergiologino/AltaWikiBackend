package com.example.wikibackend.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user_space_access")
public class UserSpaceAccess {

    @Id
    @GeneratedValue
    private UUID id = UUID.randomUUID();

    @ManyToOne //(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne //(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessType accessType;

    // Конструкторы, геттеры и сеттеры

    public UserSpaceAccess() {}

    public UserSpaceAccess(User user, Space space, AccessType accessType) {
        this.user = user;
        this.space = space;
        this.accessType = accessType;
    }

    // Геттеры и сеттеры

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }
}

