package com.example.wikibackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpaceDTO extends BaseDTO {
    private String spaceName;
    private String description;

    private String author;

    public SpaceDTO() {}

    public SpaceDTO(String spaceName, String description, String organization) {
        super(organization);
        this.spaceName = spaceName;
        this.description = description;
    }
    public String getName() {
        return spaceName;
    }

    public void setName(String name) {
        this.spaceName = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
