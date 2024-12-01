package com.example.wikibackend.repository;

import com.example.wikibackend.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document<Collection<E>>, UUID> {
    List<Document<Collection<E>>> findByParentId(UUID parentId);

    List <Document<Collection<E>>> findByTitleContaining(String name);
}

