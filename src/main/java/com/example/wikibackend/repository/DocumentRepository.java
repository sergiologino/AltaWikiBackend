package com.example.wikibackend.repository;

import com.example.wikibackend.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByParentId(Long parentId);

    List <Document> findByTitleContaining(String name);
}

