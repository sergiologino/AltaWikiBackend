package com.example.wikibackend.service;

import com.example.wikibackend.config.SwitchSchema;
import com.example.wikibackend.dto.SpaceDTO;
import com.example.wikibackend.mapper.SpaceMapper;
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
    private final SchemaService schemaService;
    private final SpaceMapper spaceMapper;

    @Autowired
    public SpaceService(SpaceRepository spaceRepository, SchemaService schemaService, SpaceMapper spaceMapper) {
        this.spaceRepository = spaceRepository;
        this.schemaService = schemaService;
        this.spaceMapper = spaceMapper;
    }

    @Transactional
    @SwitchSchema
    public List<Space> getAllSpaces(UUID organizationId) {
        schemaService.setSchema(organizationId);
        return spaceRepository.findAll();
    }

    @Transactional
    @SwitchSchema
    public SpaceDTO findSpaceById(UUID organizationId, UUID id) {
        schemaService.setSchema(organizationId);
        Optional<Space> space = spaceRepository.findById(id);
        Space foundedSpace = space.orElse(new Space());
        SpaceDTO spaceDTO = new SpaceDTO();
        spaceDTO =spaceMapper.toDTO(foundedSpace);
        return spaceDTO;
    }

    @Transactional
    @SwitchSchema
    public Space addSpace(SpaceDTO spaceDTO) {
        Space space = new Space();
        space.setName(spaceDTO.getName());
        space.setDescription(spaceDTO.getDescription());
        space.setAuthor(spaceDTO.getAuthorId());
        space.setCreatedAt(LocalDateTime.now());
        schemaService.setSchema(spaceDTO.getOrganizationId());
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
            schemaService.setSchema(spaceDTO.getOrganizationId());
            return spaceRepository.save(space);
        } else {
            throw new IllegalArgumentException("Space not found");
        }
    }
}

