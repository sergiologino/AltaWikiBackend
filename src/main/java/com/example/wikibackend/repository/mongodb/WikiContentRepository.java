package com.example.wikibackend.repository.mongodb;

import com.example.wikibackend.model.mongodb.WikiContent;
import com.example.wikibackend.model.DocumentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface WikiContentRepository extends MongoRepository<WikiContent, String> {

    List<WikiContent> findByDocumentId(Long documentId);

    List<WikiContent> findByDocumentIdAndStatus(Long documentId, DocumentStatus status);

    Optional<WikiContent> findByDocumentIdAndStatusAndUserId(Long documentId, DocumentStatus status, Long userId);

    Optional<WikiContent> findByDocumentIdAndStatusAndUserIdIsNull(Long documentId, DocumentStatus status);
}


