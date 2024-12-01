package com.example.wikibackend.mapper;

import com.example.wikibackend.dto.SpaceDTO;
import com.example.wikibackend.dto.UserDTO;
import com.example.wikibackend.model.Space;
import com.example.wikibackend.model.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
public class SpaceMapper {

    // Преобразование DTO в сущность
    public Space toEntity(SpaceDTO spaceDTO) {
        Space newSpace = new Space();
        //organization.setID(organizationDTO.getId());
        newSpace.setDescription(spaceDTO.getDescription());
        newSpace.setAuthor(spaceDTO.getAuthorId());
        newSpace.setName(spaceDTO.getSpaceName());

        return newSpace;
    }

    // Преобразование сущности  в DTO
    public SpaceDTO toDTO(Space space) {
        SpaceDTO newSpaceDTO =new SpaceDTO();
        newSpaceDTO.setDescription(space.getDescription());
        newSpaceDTO.setAuthorId(space.getAuthor());
        newSpaceDTO.setSpaceName(space.getName());
        return newSpaceDTO;
    }
}

