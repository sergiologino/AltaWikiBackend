package com.example.wikibackend.dto;

public class UserRoleDTO {
    private Long userId;
    private Long roleId;

    // Конструкторы, геттеры и сеттеры

    public UserRoleDTO() {}

    public UserRoleDTO(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
