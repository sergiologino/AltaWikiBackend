package com.example.wikibackend.service;

import com.example.wikibackend.config.SwitchSchema;
import com.example.wikibackend.dto.SpaceDTO;
import com.example.wikibackend.model.Space;
import com.example.wikibackend.repository.SpaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SpaceService {

    private final SpaceRepository spaceRepository;

    @Autowired
    public SpaceService(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

   @SwitchSchema
    public List<Space> getAllSpaces() {
        return spaceRepository.findAll();
    }

    @SwitchSchema
    public Space findSpaceById(UUID id) {
        Optional<Space> space = spaceRepository.findById(id);
        return space.orElse(new Space());
    }

    @Transactional
    @SwitchSchema
    public Space addSpace(SpaceDTO spaceDTO) {
        Space space = new Space();
        space.setName(spaceDTO.getName());
        space.setDescription(spaceDTO.getDescription());
        space.setAuthor(spaceDTO.getAuthorId());
        space.setCreatedAt(LocalDateTime.now());

        return spaceRepository.save(space);
    }

    @Transactional
    @SwitchSchema
    public Space updateSpace(UUID Spaceid, SpaceDTO spaceDTO) {
        Optional<Space> optionalSpace = spaceRepository.findById(Spaceid);
        if (optionalSpace.isPresent()) {
            Space space = optionalSpace.get();
            space.setName(spaceDTO.getName());
            space.setDescription(spaceDTO.getDescription());
            space.setAuthor(spaceDTO.getAuthorId());
            return spaceRepository.save(space);
        } else {
            throw new IllegalArgumentException("Space not found");
        }
    }
}

