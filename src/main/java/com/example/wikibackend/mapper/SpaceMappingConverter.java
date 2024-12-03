package com.example.wikibackend.mapper;

import com.example.wikibackend.dto.SpaceDTO;
import com.example.wikibackend.model.Space;

public interface SpaceMappingConverter {
    Space fromDTO(SpaceDTO dto);

    SpaceDTO toDTO(Space space);
}
