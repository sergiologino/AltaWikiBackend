package com.example.wikibackend.repository.mongodb;

import com.example.wikibackend.model.mongodb.WikiContent;
import com.example.wikibackend.model.DocumentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WikiContentRepository extends MongoRepository<WikiContent, String> {

    List<WikiContent> findByDocumentId(UUID documentId);

    List<WikiContent> findByDocumentIdAndStatus(UUID documentId, DocumentStatus status);

    Optional<WikiContent> findByDocumentIdAndStatusAndUserId(UUID documentId, DocumentStatus status, UUID userId);

    Optional<WikiContent> findByDocumentIdAndStatusAndUserIdIsNull(UUID documentId, DocumentStatus status);
}


