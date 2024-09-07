package com.example.wikibackend.service;

import com.example.wikibackend.dto.SpaceDTO;
import com.example.wikibackend.model.Space;
import com.example.wikibackend.repository.SpaceRepository;
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

    public List<Space> getAllSpaces() {
        return spaceRepository.findAll();
    }

    public Space addSpace(SpaceDTO spaceDTO) {
        Space space = new Space();
        space.setName(spaceDTO.getName());
        space.setAuthor(spaceDTO.getAuthor());
        space.setCreatedAt(LocalDateTime.now());
        return spaceRepository.save(space);
    }

    public Space updateSpace(UUID id, SpaceDTO spaceDTO) {
        Optional<Space> optionalSpace = spaceRepository.findById(id);
        if (optionalSpace.isPresent()) {
            Space space = optionalSpace.get();
            space.setName(spaceDTO.getName());
            space.setAuthor(spaceDTO.getAuthor());
            return spaceRepository.save(space);
        } else {
            throw new IllegalArgumentException("Space not found");
        }
    }
}

