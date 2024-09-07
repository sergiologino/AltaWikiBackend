package com.example.wikibackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO extends BaseDTO {
    private String roleName;

    public RoleDTO() {}

    public RoleDTO(String roleName, String organization) {
        super(organization);
        this.roleName = roleName;
    }
}

