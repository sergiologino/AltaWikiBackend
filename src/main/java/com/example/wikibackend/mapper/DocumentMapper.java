package com.example.wikibackend.mapper;

import com.example.wikibackend.dto.DocumentDTO;
import com.example.wikibackend.model.Document;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class DocumentMapper {

    public DocumentDTO toDTO(Document entity) {
        if (entity == null) {
            return null;
        }

        DocumentDTO dto = new DocumentDTO();
        //dto.setId(entity.getId().toString());
        dto.setTitle(entity.getTitle());
        //dto.setContent(entity.getContent());
        //dto.setOrganization(entity.getOrganization());
        dto.setAuthor(entity.getAuthor());
        dto.setParentId(entity.getParent());
        return dto;
    }

    public Document toEntity(DocumentDTO dto) {
        if (dto == null) {
            return null;
        }

        Document entity = new Document();
//        entity.setId(dto.getId() != null ? UUID.fromString(dto.getId()) : UUID.randomUUID());
        entity.setTitle(dto.getTitle());
//        entity.setContent(dto.getContent());
//        entity.setOrganization(dto.getOrganization());
        entity.setAuthor(dto.getAuthor());
        entity.setParent(dto.getParentId());
        return entity;
    }
}
