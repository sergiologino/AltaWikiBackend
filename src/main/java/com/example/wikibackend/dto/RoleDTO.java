package com.example.wikibackend.dto;

public class RoleDTO {
    private String name;

    // Конструкторы, геттеры и сеттеры

    public RoleDTO() {}

    public RoleDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

