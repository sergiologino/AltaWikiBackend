package com.example.wikibackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpaceDTO extends BaseDTO {
    private String spaceName;
    private String description;

    public SpaceDTO() {}

    public SpaceDTO(String spaceName, String description, String organization) {
        super(organization);
        this.spaceName = spaceName;
        this.description = description;
    }
}
