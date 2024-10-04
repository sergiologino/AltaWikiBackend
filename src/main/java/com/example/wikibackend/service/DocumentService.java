package com.example.wikibackend.service;

import com.example.wikibackend.config.SwitchSchema;
import com.example.wikibackend.dto.DocumentDTO;
import com.example.wikibackend.model.Document;
import com.example.wikibackend.model.mongodb.WikiContent;
import com.example.wikibackend.repository.DocumentRepository;
import com.example.wikibackend.repository.mongodb.WikiContentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final WikiContentRepository wikiContentRepository;
    private final WikiContentService wikiContentService;

    @Autowired
    public DocumentService(DocumentRepository documentRepository, WikiContentRepository wikiContentRepository, WikiContentService wikiContentService) {
        this.documentRepository = documentRepository;
        this.wikiContentRepository = wikiContentRepository;
        this.wikiContentService = wikiContentService;
    }

    @Transactional
    @SwitchSchema
    public Document createDocument(DocumentDTO documentDTO) {
        Document document = new Document();
        document.setTitle(documentDTO.getTitle());
        document.setAuthor(documentDTO.getAuthorId());
        // Сохранение основного документа в реляционной БД
        document = documentRepository.save(document);

        // Создание и сохранение содержимого документа в MongoDB
        WikiContent wikiContent = new WikiContent();
        wikiContent.setDocumentId(document.getId());
        wikiContent.setContent(documentDTO.getContent());
        wikiContentRepository.save(wikiContent);

        return document;
    }

    @Transactional
    @SwitchSchema
    public Document updateDocument(UUID id, DocumentDTO documentDTO) {
        Document document = documentRepository.findById(id).orElseThrow(() -> new RuntimeException("Документ не найден"));
        document.setTitle(documentDTO.getTitle());
        document.setAuthor(documentDTO.getAuthorId());

        // Обновление основного документа в реляционной БД
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
    public boolean deleteDocument(UUID id) {
        documentRepository.deleteById(id);
        //wikiContentRepository.deleteByDocumentId(id);// установить деактуализацию вместо удаления
        return true;
    }

    @SwitchSchema
    public List<Document> getAllDocuments() {


        return documentRepository.findAll();
    }

    @SwitchSchema
    public Document getDocumentById(UUID id) {
        return documentRepository.findById(id).orElse(null);
    }
}
