package com.example.wikibackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    private boolean enabled;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserSpaceAccess> spaceAccesses = new HashSet<>();

    // Конструкторы, геттеры и сеттеры

    public User() {}

    // Другие конструкторы, геттеры и сеттеры...

    public Set<UserSpaceAccess> getSpaceAccesses() {
        return spaceAccesses;
    }

    public void setSpaceAccesses(Set<UserSpaceAccess> spaceAccesses) {
        this.spaceAccesses = spaceAccesses;
    }

    public void setUsername(String username) {
        this.username=username;  }
}
