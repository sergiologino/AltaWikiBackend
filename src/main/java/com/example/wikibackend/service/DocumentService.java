package com.example.wikibackend.service;

import com.example.wikibackend.model.Document;
import com.example.wikibackend.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    // Получение дерева всех документов
    public List<Document> getDocumentTree() {
        return documentRepository.findAll();
    }

    // Получение поддерева документов
    public List<Document> getSubDocumentTree(Long parentId) {
        return documentRepository.findByParentId(parentId);
    }

    // Поиск документа по ID
    public Optional<Document> findDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    // Поиск документа по заголовку
    public List<Document> searchDocumentByTitle(String title) {
        return documentRepository.findByTitleContaining(title);
    }

    // Сохранение документа
    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    // Удаление документа
    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }
}
