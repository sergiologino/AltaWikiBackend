package com.example.wikibackend.dto;

public class SpaceDTO {
    private String name;
    private String author;

    // Конструкторы, геттеры и сеттеры

    public SpaceDTO() {}

    public SpaceDTO(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
