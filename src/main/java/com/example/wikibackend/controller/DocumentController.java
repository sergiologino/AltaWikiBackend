package com.example.wikibackend.controller;

import com.example.wikibackend.dto.DocumentDTO;
import com.example.wikibackend.model.Document;
import com.example.wikibackend.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Operation(summary = "Получить все документы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех документов",
                            content = @Content(schema = @Schema(implementation = Document.class)))
            })
    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @Operation(summary = "Добавить новый документ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DocumentDTO.class),
                            examples = @ExampleObject(value = "{\"title\": \"Документ 1\", \"status\": \"ACTIVE\", \"author\": \"Автор\", \"spaceId\": 1, \"parentId\": null}"))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Документ успешно добавлен",
                            content = @Content(schema = @Schema(implementation = Document.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
            })
    @PostMapping
    public ResponseEntity<Document> addDocument(@RequestBody DocumentDTO documentDTO) {
        Document document = documentService.addDocument(documentDTO);
        return ResponseEntity.status(201).body(document);
    }

    @Operation(summary = "Изменить документ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DocumentDTO.class),
                            examples = @ExampleObject(value = "{\"title\": \"Обновленный документ\", \"status\": \"OUTDATED\", \"author\": \"Автор\", \"spaceId\": 1, \"parentId\": null}"))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Документ успешно изменен",
                            content = @Content(schema = @Schema(implementation = Document.class))),
                    @ApiResponse(responseCode = "404", description = "Документ не найден")
            })
    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody DocumentDTO documentDTO) {
        Document updatedDocument = documentService.updateDocument(id, documentDTO);
        return ResponseEntity.ok(updatedDocument);
    }
}

