package com.example.wikibackend.dto;

import lombok.*;

import java.util.UUID;


public class RoleAccessDTO {
    private UUID roleId;
    private String roleName;
    private String accessType;

    public RoleAccessDTO() {}

    public RoleAccessDTO(UUID roleId, String roleName, String accessType) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.accessType = accessType;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }
}
