package com.example.wikibackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class SpaceDTO extends BaseDTO {
    @Setter
    private String spaceName;
    @Setter
    private String description;

    private UUID authorId;

    public SpaceDTO() {}

    public SpaceDTO(String spaceName, String description, UUID organizationId, UUID authorId) {
        super(organizationId);
        this.spaceName = spaceName;
        this.description = description;
        this.authorId = authorId;
    }
    public String getName() {
        return spaceName;
    }

    public void setName(String name) {
        this.spaceName = name;
    }

    public void setAuthor(UUID authorId) {
        this.authorId = authorId;
    }

}
