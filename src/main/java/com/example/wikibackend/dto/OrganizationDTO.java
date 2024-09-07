package com.example.wikibackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationDTO extends BaseDTO {
    private String name;
    private String description;

    public OrganizationDTO() {}

    public OrganizationDTO(String name, String description, String organization) {
        super(organization);
        this.name = name;
        this.description = description;
    }
    public String getOrganizationName() {
        return name;
    }

    public void setOrganizationName(String organizationName) {
        this.name = organizationName;
    }
}

