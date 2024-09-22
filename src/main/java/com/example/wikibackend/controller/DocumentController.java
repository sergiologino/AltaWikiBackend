package com.example.wikibackend.controller;

import com.example.wikibackend.config.TenantContext;
import com.example.wikibackend.dto.DocumentDTO;
import com.example.wikibackend.model.Document;
import com.example.wikibackend.service.DocumentService;
import com.example.wikibackend.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final OrganizationService organizationService;

    @Autowired
    public DocumentController(DocumentService documentService, OrganizationService organizationService) {
        this.documentService = documentService;
        this.organizationService = organizationService;
    }

    @Operation(summary = "Создание нового документа",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DocumentDTO.class),
                            examples = @ExampleObject(value = "{\"organizationId\": 10, \"title\": \"Документ\", \"content\": \"Содержимое документа\"}"))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Документ успешно создан",
                            content = @Content(schema = @Schema(implementation = Document.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
            })
    @PostMapping
    public ResponseEntity<?> createDocument(@RequestParam Integer organizationId, @RequestBody DocumentDTO documentDTO) {
        if (organizationId == null) {
            return ResponseEntity.badRequest().body("organizationId должен быть указан.");
        }
        Long aliasOrg = organizationService.getAlias(organizationId);
        TenantContext.setCurrentTenant(aliasOrg);
        Document document = documentService.createDocument(documentDTO);
        TenantContext.clear();
        return ResponseEntity.status(201).body(document);
    }

    @Operation(summary = "Обновление документа",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DocumentDTO.class),
                            examples = @ExampleObject(value = "{\"title\": \"Обновленный документ\", \"content\": \"Обновленное содержимое\"}"))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Документ успешно обновлен",
                            content = @Content(schema = @Schema(implementation = Document.class))),
                    @ApiResponse(responseCode = "404", description = "Документ не найден")
            })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDocument(@RequestParam Integer organizationId, @PathVariable UUID id, @RequestBody DocumentDTO documentDTO) {
        if (organizationId == null) {
            return ResponseEntity.badRequest().body("organizationId должен быть указан.");
        }
        Long aliasOrg = organizationService.getAlias(organizationId);
        TenantContext.setCurrentTenant(aliasOrg);
        Document updatedDocument = documentService.updateDocument(id, documentDTO);
        TenantContext.clear();
        return ResponseEntity.ok(updatedDocument);
    }

    @Operation(summary = "Удаление документа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Документ успешно удален"),
                    @ApiResponse(responseCode = "404", description = "Документ не найден")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@RequestParam Integer organizationId, @PathVariable UUID id) {
        if (organizationId == null) {
            return ResponseEntity.badRequest().body("organizationId должен быть указан.");
        }
        Long aliasOrg = organizationService.getAlias(organizationId);
        TenantContext.setCurrentTenant(aliasOrg);
        boolean isDeleted = documentService.deleteDocument(id);
        TenantContext.clear();
        if (isDeleted) {
            return ResponseEntity.ok("Документ успешно удален");
        } else {
            return ResponseEntity.status(404).body("Документ не найден");
        }
    }

    @Operation(summary = "Получение всех документов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех документов",
                            content = @Content(schema = @Schema(implementation = Document.class)))
            })
    @GetMapping
    public ResponseEntity<?> getAllDocuments(@RequestParam Integer organizationId) {
        if (organizationId == null) {
            return ResponseEntity.badRequest().body("organizationId должен быть указан.");
        }
        Long aliasOrg = organizationService.getAlias(organizationId);
        TenantContext.setCurrentTenant(aliasOrg);
        List<Document> documents = documentService.getAllDocuments();
        TenantContext.clear();
        return ResponseEntity.ok(documents);
    }

    @Operation(summary = "Получение документа по ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Документ найден",
                            content = @Content(schema = @Schema(implementation = Document.class))),
                    @ApiResponse(responseCode = "404", description = "Документ не найден")
            })
    @GetMapping("/{id}")
    public ResponseEntity<?> getDocumentById(@RequestParam Integer organizationId, @PathVariable UUID id) {
        if (organizationId == null) {
            return ResponseEntity.badRequest().body("organizationId должен быть указан.");
        }
        Long aliasOrg = organizationService.getAlias(organizationId);
        TenantContext.setCurrentTenant(aliasOrg);
        Document document = documentService.getDocumentById(id);
        TenantContext.clear();
        if (document != null) {
            return ResponseEntity.ok(document);
        } else {
            return ResponseEntity.status(404).body("Документ не найден");
        }
    }
}
