package com.example.wikibackend.repository;

import com.example.wikibackend.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> findByParentId(Long parentId);

    List <Document> findByTitleContaining(String name);
}

