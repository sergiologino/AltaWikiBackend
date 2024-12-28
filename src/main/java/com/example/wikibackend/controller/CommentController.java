package com.example.wikibackend.controller;

import com.example.wikibackend.dto.CommentDTO;
import com.example.wikibackend.model.Comment;
import com.example.wikibackend.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Создание комментария",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Комментарий успешно создан",
                            content = @Content(schema = @Schema(implementation = CommentDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            })
    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        CommentDTO createdComment = commentService.createComment(commentDTO);
        return ResponseEntity.status(201).body(createdComment);
    }

    @Operation(summary = "Редактирование комментария",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий успешно обновлен"),
                    @ApiResponse(responseCode = "404", description = "Комментарий не найден")
            })
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable UUID id, @RequestBody String content) {
        CommentDTO updatedComment = commentService.updateComment(id, content);
        return ResponseEntity.ok(updatedComment);
    }

    @Operation(summary = "Установка статуса 'Решен'",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий помечен как решён"),
                    @ApiResponse(responseCode = "404", description = "Комментарий не найден")
            })
    @PatchMapping("/{id}/resolve")
    public ResponseEntity<CommentDTO> resolveComment(@PathVariable UUID id) {
        CommentDTO resolvedComment = commentService.resolveComment(id);
        return ResponseEntity.ok(resolvedComment);
    }

    @Operation(summary = "Получение всех комментариев для версии документа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список комментариев",
                            content = @Content(schema = @Schema(implementation = CommentDTO.class)))
            })
    @GetMapping
    public ResponseEntity<List<CommentDTO>> getComments(@RequestParam UUID documentId, @RequestParam String documentVersion) {
        List<CommentDTO> comments = commentService.getCommentsByDocument(documentId, documentVersion);
        return ResponseEntity.ok(comments);
    }
}


