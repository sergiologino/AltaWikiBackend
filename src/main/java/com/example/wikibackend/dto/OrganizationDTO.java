package com.example.wikibackend.dto;

public class OrganizationDTO {
    private String organizationName;

    // Конструкторы, геттеры и сеттеры

    public OrganizationDTO() {}

    public OrganizationDTO(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}

