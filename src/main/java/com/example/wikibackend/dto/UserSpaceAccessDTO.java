package com.example.wikibackend.dto;

import com.example.wikibackend.model.AccessType;

public class UserSpaceAccessDTO {

    private Long userId;
    private Long spaceId;
    private AccessType accessType;

    // Конструкторы, геттеры и сеттеры

    public UserSpaceAccessDTO() {}

    public UserSpaceAccessDTO(Long userId, Long spaceId, AccessType accessType) {
        this.userId = userId;
        this.spaceId = spaceId;
        this.accessType = accessType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }
}

