package com.example.wikibackend.mapper;

import com.example.wikibackend.dto.SpaceDTO;
import com.example.wikibackend.model.Space;
import org.springframework.stereotype.Component;

@Component(value = "spaceConverter")
public class SpaceConverter extends AbstractConverter {
    @Override
    public Space toEntity(SpaceDTO dto) {
        if (dto == null) {
            return null;
        } else {
            Space space= new Space();
            // Assign all values you wanted to consume
            // of the following form
            // coin.setYourAttribite(dto.getYourAttribute())
            return space;
        }
    }

    @Override
    public SpaceDTO toDTO(Space space) {
        if (space == null) {
            return null;
        } else {
            SpaceDTO newSpaceDTO =new SpaceDTO();
            newSpaceDTO.setDescription(space.getDescription());
            newSpaceDTO.setAuthorId(space.getAuthor());
            newSpaceDTO.setSpaceName(space.getName());
            return newSpaceDTO;
        }
    }
}

