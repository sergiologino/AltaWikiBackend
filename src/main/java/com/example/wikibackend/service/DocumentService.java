package com.example.wikibackend.service;

import com.example.wikibackend.config.SwitchSchema;
import com.example.wikibackend.dto.DocumentAccessDTO;
import com.example.wikibackend.dto.DocumentDTO;
import com.example.wikibackend.dto.RoleAccessDTO;
import com.example.wikibackend.mapper.DocumentMapper;
import com.example.wikibackend.mapper.SpaceMapper;
import com.example.wikibackend.model.*;
import com.example.wikibackend.model.mongodb.WikiContent;
import com.example.wikibackend.repository.DocumentRepository;
import com.example.wikibackend.repository.UserRepository;
import com.example.wikibackend.repository.mongodb.WikiContentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final WikiContentRepository wikiContentRepository;
    private final WikiContentService wikiContentService;
    private final SpaceService spaceService;
    private final SchemaService schemaService;
    private final SpaceMapper spaceMapper;
    private final DocumentMapper documentMapper;
    private final UserRepository userRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository, WikiContentRepository wikiContentRepository, WikiContentService wikiContentService, SpaceService spaceService, SchemaService schemaService, SpaceMapper spaceMapper, DocumentMapper documentMapper, UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.wikiContentRepository = wikiContentRepository;
        this.wikiContentService = wikiContentService;
        this.spaceService = spaceService;
        this.schemaService = schemaService;
        this.spaceMapper = spaceMapper;
        this.documentMapper = documentMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    @SwitchSchema
    public Document createDocument(DocumentDTO documentDTO) {
        Document document = new Document();
        document.setTitle(documentDTO.getTitle());
        document.setAuthor(documentDTO.getAuthorId());
        document.setCreatedAt(LocalDateTime.now());
        document.setLastModifiedAt(LocalDateTime.now());
        document.setStatus(documentDTO.getStatus());
        document.setSpace(documentDTO.getSpaceId());
        document.setParent(documentDTO.getParentId());

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
        document.setSpace(documentDTO.getSpaceId());
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

    @Transactional
    @SwitchSchema
    public List<Document> getAllDocuments(UUID organizationId) {
        schemaService.setSchema(organizationId);
        return documentRepository.findAll();

    }

    @SwitchSchema
    public DocumentDTO getDocumentById(UUID organizationId, UUID id) {
        schemaService.setSchema(organizationId);
        Document existingDocument=documentRepository.findById(id).orElse(null);
        DocumentDTO documentDTO = documentMapper.toDTO(existingDocument);
        Optional<WikiContent> existingContent =  wikiContentRepository.findByDocumentId(id).stream().findFirst();
        if(!(existingContent ==null) && !(existingDocument ==null)){
            documentDTO.setContent(existingContent.get().getContent());

        }else{
            documentDTO.setContent(" no content");
        };
        return documentDTO;
    }

    @SwitchSchema
    public Document getVersionsByDocumentById(UUID organizationId, UUID id) {
        schemaService.setSchema(organizationId);
        WikiContent wikiContent = new WikiContent();
        List<String> existingVersions= wikiContentRepository.findByDocumentId(id).stream()
                .map(WikiContent::getVersion)
                .collect(Collectors.toList());


        return documentRepository.findById(id).orElse(null);
    }

    @Transactional
    @SwitchSchema
    public List<DocumentAccessDTO> getDocumentsForUserWithAccess(UUID userId, UUID organizationId) {
        // Установка схемы для организации
        schemaService.setSchema(organizationId);

        // Получение пользователя и его ролей
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Set<Role> roles = user.getRoles();

        // Сбор всех доступов по ролям
        List<RoleDocumentAccess> roleDocumentAccesses = roles.stream()
                .flatMap(role -> role.getDocumentAccesses().stream())
                .collect(Collectors.toList());

        // Рекурсивное определение доступов, включая родителей и секции
        Map<UUID, DocumentAccessDTO> documentAccessMap = new HashMap<>();
        for (RoleDocumentAccess access : roleDocumentAccesses) {
            UUID documentId = access.getDocument().getId();
            processDocumentAccess(access.getDocument(), access.getAccessType(), documentAccessMap, roles);
        }

        // Исключение документов с доступом DENIED
        return documentAccessMap.values().stream()
                .filter(dto -> !dto.getAccessType().equals("DENIED"))
                .collect(Collectors.toList());
    }

    private void processDocumentAccess(Document document, AccessType accessType,
                                       Map<UUID, DocumentAccessDTO> documentAccessMap, Set<Role> roles) {
        // Если доступ уже установлен и текущий уровень ниже, ничего не меняем
        if (documentAccessMap.containsKey(document.getId()) &&
                documentAccessMap.get(document.getId()).getAccessType().equals("DENIED")) {
            return;
        }

        // Устанавливаем или обновляем доступ
        DocumentAccessDTO dto = new DocumentAccessDTO();
        dto.setDocument(documentMapper.toDTO(document));
        dto.setAccessType(accessType == AccessType.READ ? "READ" : "FULL_ACCESS");
        documentAccessMap.put(document.getId(), dto);

        // Рекурсивно обрабатываем родителя
        if (document.getParent() != null) {
            Document parentDocument = documentRepository.findById(document.getParent())
                    .orElseThrow(() -> new RuntimeException("Родительский документ не найден"));
            processDocumentAccess(parentDocument, accessType, documentAccessMap, roles);
        }

        // Обрабатываем все документы в той же секции (если применимо)
        List<Document> sectionDocuments = documentRepository.findBySpaceId(document.getSpaceId());
        for (Document sectionDocument : sectionDocuments) {
            if (!documentAccessMap.containsKey(sectionDocument.getId())) {
                processDocumentAccess(sectionDocument, accessType, documentAccessMap, roles);
            }
        }
    }

    @Transactional
    @SwitchSchema
    public List<RoleAccessDTO> getRolesForDocument(UUID documentId, UUID organizationId) {
        // Установка схемы для организации
        schemaService.setSchema(organizationId);

        // Получение документа
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Документ не найден"));

        // Рекурсивное получение ролей, влияющих на документ
        Set<RoleDocumentAccess> roleAccesses = new HashSet<>();
        collectRoleAccesses(document, roleAccesses);

        // Преобразование в DTO
        return roleAccesses.stream()
                .map(access -> {
                    RoleAccessDTO dto = new RoleAccessDTO();
                    dto.setRoleId(access.getRole().getId());
                    dto.setRoleName(access.getRole().getRole_name());
                    dto.setAccessType(access.getAccessType().toString());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private void collectRoleAccesses(Document document, Set<RoleDocumentAccess> roleAccesses) {
        // Добавляем текущие роли
        roleAccesses.addAll(document.getRoleAccesses());

        // Рекурсивно обрабатываем родителя
        if (document.getParent() != null) {
            Document parentDocument = documentRepository.findById(document.getParent())
                    .orElseThrow(() -> new RuntimeException("Родительский документ не найден"));
            collectRoleAccesses(parentDocument, roleAccesses);
        }
    }

}
