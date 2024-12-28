package com.example.wikibackend.controller;

import com.example.wikibackend.model.mongodb.WikiContent;
import com.example.wikibackend.service.WikiContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final WikiContentService wikiContentService;

    @Autowired
    public ContentController(WikiContentService wikiContentService) {
        this.wikiContentService = wikiContentService;
    }

    @Operation(summary = "Получение всех версий документа по его ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список версий документа",
                            content = @Content(schema = @Schema(implementation = WikiContent.class)))
            })
    @GetMapping("/versions/{documentId}")
    public ResponseEntity<List<WikiContent>> getAllVersionsByDocumentId(@PathVariable UUID documentId) {
        List<WikiContent> versions = wikiContentService.getAllVersionsByDocumentId(documentId);
        return ResponseEntity.ok(versions);
    }

    @Operation(summary = "Получение текущей активной версии документа по его ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Текущая активная версия документа",
                            content = @Content(schema = @Schema(implementation = WikiContent.class))),
                    @ApiResponse(responseCode = "404", description = "Активная версия не найдена")
            })
    @GetMapping("/active/{documentId}")
    public ResponseEntity<WikiContent> getCurrentActiveVersionByDocumentId(@PathVariable UUID documentId) {
        Optional<WikiContent> activeVersion = wikiContentService.getCurrentVersionByDocumentId(documentId);
        if (activeVersion.isPresent()) {
            return ResponseEntity.ok(activeVersion.get());
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @Operation(summary = "Получение черновика документа по его ID для конкретного пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Черновик документа",
                            content = @Content(schema = @Schema(implementation = WikiContent.class))),
                    @ApiResponse(responseCode = "404", description = "Черновик не найден")
            })
    @GetMapping("/draft/{documentId}")
    public ResponseEntity<?> getDraftByDocumentIdAndUserId(@PathVariable UUID documentId, @RequestParam UUID userId) {
        Optional<WikiContent> draft = wikiContentService.getDraftByDocumentIdAndUserId(documentId, userId);
        if (draft.isPresent()){
            return ResponseEntity.ok(draft.get());
        }else{
            return ResponseEntity.status(404).build();
        }
    }
}

