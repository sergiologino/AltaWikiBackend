package com.example.wikibackend.service;

import com.example.wikibackend.config.SwitchSchema;
import com.example.wikibackend.dto.DocumentDTO;
import com.example.wikibackend.mapper.SpaceMapper;
import com.example.wikibackend.model.Document;
import com.example.wikibackend.model.mongodb.WikiContent;
import com.example.wikibackend.repository.DocumentRepository;
import com.example.wikibackend.repository.mongodb.WikiContentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final WikiContentRepository wikiContentRepository;
    private final WikiContentService wikiContentService;
    private final SpaceService spaceService;
    private final SchemaService schemaService;
    private final SpaceMapper spaceMapper;

    @Autowired
    public DocumentService(DocumentRepository documentRepository, WikiContentRepository wikiContentRepository, WikiContentService wikiContentService, SpaceService spaceService, SchemaService schemaService, SpaceMapper spaceMapper) {
        this.documentRepository = documentRepository;
        this.wikiContentRepository = wikiContentRepository;
        this.wikiContentService = wikiContentService;
        this.spaceService = spaceService;
        this.schemaService = schemaService;
        this.spaceMapper = spaceMapper;
    }

    @Transactional
    @SwitchSchema
    public Document createDocument(UUID organizationId, DocumentDTO documentDTO) {
        Document document = new Document();
        document.setTitle(documentDTO.getTitle());
        document.setAuthor(documentDTO.getAuthorId());
        document.setCreatedAt(LocalDateTime.now());
        document.setLastModifiedAt(LocalDateTime.now());
        document.setStatus(documentDTO.getStatus());
        document.setSpace(spaceMapper.toEntity(spaceService.findSpaceById(documentDTO.getOrganizationId(), documentDTO.getSpaceId())));

        // Сохранение основного документа в реляционной БД
        schemaService.setSchema(documentDTO.getOrganizationId());
        document = documentRepository.save(document);



        // Создание и сохранение содержимого документа в MongoDB
        WikiContent wikiContent = new WikiContent();
        wikiContent.setDocumentId(document.getId());
        wikiContent.setContent(documentDTO.getContent());
        wikiContent.setStatus(documentDTO.getStatus());
       // wikiContent.setVersion(document.g);
        wikiContentRepository.save(wikiContent);

        return document;
    }

    @Transactional
    @SwitchSchema
    public Document updateDocument( UUID id, DocumentDTO documentDTO) {
        Document document = documentRepository.findById(id).orElseThrow(() -> new RuntimeException("Документ не найден"));
        document.setTitle(documentDTO.getTitle());
        document.setAuthor(documentDTO.getAuthorId());
        document.setLastModifiedAt(LocalDateTime.now());

        // Обновление основного документа в реляционной БД
        document.setStatus(documentDTO.getStatus());
        document.setSpace(spaceMapper.toEntity(spaceService.findSpaceById(documentDTO.getOrganizationId(),documentDTO.getSpaceId())));
        schemaService.setSchema(documentDTO.getOrganizationId());
        document = documentRepository.save(document);

        // Обновление содержимого документа в MongoDB
        WikiContent wikiContent = wikiContentRepository.findByDocumentId(id).stream().findFirst()
                .orElse(new WikiContent()); // Создаем новый, если не существует
        wikiContent.setDocumentId(document.getId());
        wikiContent.setContent(documentDTO.getContent());
        wikiContentRepository.save(wikiContent);

        return document;
    }

    @Transactional
    @SwitchSchema
    public boolean deleteDocument(UUID organizationId, UUID id) {
        schemaService.setSchema(organizationId);
        documentRepository.deleteById(id);
        //wikiContentRepository.deleteByDocumentId(id);// установить деактуализацию вместо удаления
        return true;
    }

    @SwitchSchema
    public List<Document> getAllDocuments(UUID organizationId) {
        schemaService.setSchema(organizationId);
        return documentRepository.findAll();

    }

    @SwitchSchema
    public Document getDocumentById(UUID organizationId, UUID id) {
        schemaService.setSchema(organizationId);
        return documentRepository.findById(id).orElse(null);
    }
}
