package com.example.wikibackend.mapper;

import com.example.wikibackend.dto.CommentDTO;
import com.example.wikibackend.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toEntity(CommentDTO dto) {
        if (dto == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setCreatedAt(dto.getCreatedAt());
        comment.setUpdatedAt(dto.getUpdatedAt());
        comment.setResolved(dto.isResolved());
        comment.setAnchor(dto.getAnchor());
        comment.setAuthorId(dto.getAuthorId());
        comment.setParentId(dto.getParentId());
        comment.setDocumentId(dto.getDocumentId());
        comment.setDocumentVersion(dto.getDocumentVersion());
        comment.setContent(dto.getContent());
        return comment;
    }

    public CommentDTO toDTO(Comment entity) {
        if (entity == null) {
            return null;
        }
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setResolved(entity.isResolved());
        dto.setAnchor(entity.getAnchor());
        dto.setAuthorId(entity.getAuthorId());
        dto.setParentId(entity.getParentId());
        dto.setDocumentId(entity.getDocumentId());
        dto.setDocumentVersion(entity.getDocumentVersion());
        dto.setContent(entity.getContent());
        return dto;
    }
}
