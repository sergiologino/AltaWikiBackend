package com.example.wikibackend.service;

import com.example.wikibackend.dto.DocumentDTO;
import com.example.wikibackend.model.Document;
import com.example.wikibackend.model.Space;
import com.example.wikibackend.repository.DocumentRepository;
import com.example.wikibackend.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final SpaceRepository spaceRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository, SpaceRepository spaceRepository) {
        this.documentRepository = documentRepository;
        this.spaceRepository = spaceRepository;
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document addDocument(DocumentDTO documentDTO) {
        Document document = new Document();
        document.setTitle(documentDTO.getTitle());
        document.setStatus(documentDTO.getStatus());
        document.setAuthor(documentDTO.getAuthor());
        document.setCreatedAt(LocalDateTime.now());
        document.setLastModifiedAt(LocalDateTime.now());

        Optional<Space> space = spaceRepository.findById(documentDTO.getSpaceId());
        space.ifPresent(document::setSpace);

        if (documentDTO.getParentId() != null) {
            Optional<Document> parent = documentRepository.findById(documentDTO.getParentId());
            parent.ifPresent(document::setParent);
        }

        return documentRepository.save(document);
    }

    public Document updateDocument(Long id, DocumentDTO documentDTO) {
        Optional<Document> optionalDocument = documentRepository.findById(id);
        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();
            document.setTitle(documentDTO.getTitle());
            document.setStatus(documentDTO.getStatus());
            document.setAuthor(documentDTO.getAuthor());
            document.setLastModifiedAt(LocalDateTime.now());

            Optional<Space> space = spaceRepository.findById(documentDTO.getSpaceId());
            space.ifPresent(document::setSpace);

            if (documentDTO.getParentId() != null) {
                Optional<Document> parent = documentRepository.findById(documentDTO.getParentId());
                parent.ifPresent(document::setParent);
            } else {
                document.setParent(null);
            }

            return documentRepository.save(document);
        } else {
            throw new IllegalArgumentException("Document not found");
        }
    }
}
