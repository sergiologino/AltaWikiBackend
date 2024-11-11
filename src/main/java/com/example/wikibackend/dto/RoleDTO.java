package com.example.wikibackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RoleDTO extends BaseDTO {
    private String roleName;
    private String roleDescription;

    public RoleDTO() {}

    public RoleDTO(String roleName, UUID organization, String roleDescription) {
        super(organization);
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }
    public String getName() {
        return roleName;
    }

    public void setName(String name) {
        this.roleName = name;
    }

    public String getDescription() {return roleDescription;}

    public void setDescription(String description) {this.roleDescription = description;}

}

