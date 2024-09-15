package com.example.wikibackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SpaceDTO extends BaseDTO {
    private String spaceName;
    private String description;

    private UUID authorId;

    public SpaceDTO() {}

    public SpaceDTO(String spaceName, String description, UUID organizationId) {
        super(organizationId);
        this.spaceName = spaceName;
        this.description = description;
    }
    public String getName() {
        return spaceName;
    }

    public void setName(String name) {
        this.spaceName = name;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthor(UUID authorId) {
        this.authorId = authorId;
    }
}
