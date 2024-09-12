package com.example.wikibackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BaseDTO {
    private Long organizationId;

    public BaseDTO() {}

    public BaseDTO(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getOrganization() {
        return organizationId;
    }

    public void setOrganization(Long organizationId) {
        this.organizationId = organizationId;
    }


}
