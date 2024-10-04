package com.example.wikibackend.dto;

import com.example.wikibackend.model.AccessType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserSpaceAccessDTO extends BaseDTO{

    private UUID userId;
    private UUID spaceId;
    private AccessType accessType;

    // Конструкторы, геттеры и сеттеры

    public UserSpaceAccessDTO() {}

    public UserSpaceAccessDTO(UUID userId, UUID spaceId, AccessType accessType, UUID organization) {
        super(organization);
        this.userId = userId;
        this.spaceId = spaceId;
        this.accessType = accessType;

    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(UUID spaceId) {
        this.spaceId = spaceId;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }
}

