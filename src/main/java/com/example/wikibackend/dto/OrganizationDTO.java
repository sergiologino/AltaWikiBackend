package com.example.wikibackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationDTO {
    private String organization;
    private Long alias;
    public OrganizationDTO() {}

    public OrganizationDTO(String organization) {
        this.organization=organization;
        //this.description = description;
    }
    public void setOrganization(String organization) {
        this.organization=organization;
    }
    public String getOrganization() {
        return organization;
    }

    public Long getAlias() {
        return alias;
    }

    public void setAlias(Long alias) {
        this.alias = alias;
    }
}

