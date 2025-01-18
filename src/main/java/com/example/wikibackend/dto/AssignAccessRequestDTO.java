package com.example.wikibackend.dto;

import com.example.wikibackend.model.AccessType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AssignAccessRequestDTO extends BaseDTO {
    private List<UUID> documentIds;
    private AccessType accessType;
}
