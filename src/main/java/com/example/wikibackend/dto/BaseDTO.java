package com.example.wikibackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BaseDTO {
    private UUID organizationId;

    public BaseDTO() {}

    public BaseDTO(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public UUID getOrganization() {
        return organizationId;
    }

    public void setOrganization(UUID organizationId) {
        this.organizationId = organizationId;
    }


}
