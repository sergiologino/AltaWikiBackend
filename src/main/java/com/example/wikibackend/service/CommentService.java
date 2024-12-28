package com.example.wikibackend.service;

import com.example.wikibackend.dto.CommentDTO;
import com.example.wikibackend.mapper.CommentMapper;
import com.example.wikibackend.model.Comment;
import com.example.wikibackend.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = commentMapper.toEntity(commentDTO);
        comment.setCreatedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDTO(savedComment);
    }

    public CommentDTO updateComment(UUID id, String content) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Комментарий не найден"));
        comment.setContent(content);
        comment.setUpdatedAt(LocalDateTime.now());
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toDTO(updatedComment);
    }

    public CommentDTO resolveComment(UUID id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Комментарий не найден"));
        comment.setResolved(true);
        comment.setUpdatedAt(LocalDateTime.now());
        Comment resolvedComment = commentRepository.save(comment);
        return commentMapper.toDTO(resolvedComment);
    }

    public List<CommentDTO> getCommentsByDocument(UUID documentId, String documentVersion) {
        return commentRepository.findByDocumentIdAndDocumentVersion(documentId, documentVersion).stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
    }
}