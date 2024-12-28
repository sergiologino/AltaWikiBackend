package com.example.wikibackend.repository;

import com.example.wikibackend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByDocumentIdAndDocumentVersion(UUID documentId, String documentVersion);

    List<Comment> findByParentId(UUID parentId);
}

