package com.example.wikibackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RoleDTO extends BaseDTO {
    private String roleName;

    public RoleDTO() {}

    public RoleDTO(String roleName, Long organization) {
        super(organization);
        this.roleName = roleName;
    }
    public String getName() {
        return roleName;
    }

    public void setName(String name) {
        this.roleName = name;
    }
}

