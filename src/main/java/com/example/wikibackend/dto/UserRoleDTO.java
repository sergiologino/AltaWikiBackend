package com.example.wikibackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserRoleDTO extends BaseDTO {
    private UUID userId;
    private UUID roleId;

    // Конструкторы, геттеры и сеттеры

    public UserRoleDTO() {}

    public UserRoleDTO(UUID userId, UUID roleId, UUID organizationId) {
        super(organizationId);
        this.userId = userId;
        this.roleId = roleId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }
}
