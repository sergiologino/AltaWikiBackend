package com.example.wikibackend.controller;

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

import java.util.Collection;
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
                            examples = @ExampleObject(value = "{\"organizationId\": \"5929cf36-101f-471f-a9b0-afbb3964cd37\", \"title\": \"Заголовок документа\", \"status\": \"ACTIVE\",\"spaceId\": \"729f370d-faa8-486e-b4ee-bd46b70e14d6\", \"authorId\": \"79ddc8b8-1057-43e5-9e2b-f49fe9917a36\", \"content\": \"Содержимое документа\"}"))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Документ успешно создан",
                            content = @Content(schema = @Schema(implementation = Document.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
            })
    @PostMapping
    public ResponseEntity<?> createDocument(@RequestBody DocumentDTO documentDTO) {

        Long aliasOrg = organizationService.getAlias(documentDTO.getOrganizationId());
        if (aliasOrg == null) {
            return ResponseEntity.badRequest().body("Не удалось определить организацию, возможно вы не авторизованы");
        }

        Document document = documentService.createDocument(documentDTO.getOrganizationId(), documentDTO);

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
    public ResponseEntity<?> updateDocument(@PathVariable UUID id, @RequestBody DocumentDTO documentDTO) {
        if (documentDTO.getOrganizationId() == null) {
            return ResponseEntity.badRequest().body("organizationId должен быть указан.");
        }
        Long aliasOrg = organizationService.getAlias(documentDTO.getOrganizationId());
        if (aliasOrg == null) {
            return ResponseEntity.badRequest().body("Не удалось определить организацию, возможно вы не авторизованы");
        }

        Document updatedDocument = documentService.updateDocument(id, documentDTO);

        return ResponseEntity.ok(updatedDocument);
    }

    @Operation(summary = "Удаление документа",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Документ успешно удален"),
                    @ApiResponse(responseCode = "404", description = "Документ не найден")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@RequestParam UUID organizationId, @PathVariable UUID id) {
        if (organizationId == null) {
            return ResponseEntity.badRequest().body("organizationId должен быть указан.");
        }
        Long aliasOrg = organizationService.getAlias(organizationId);
        if (aliasOrg == null) {
            return ResponseEntity.badRequest().body("Не удалось определить организацию, возможно вы не авторизованы");
        }

        boolean isDeleted = documentService.deleteDocument( organizationId, id);

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
    public ResponseEntity<List<Document>> getAllDocuments(@RequestParam UUID organizationId) {

        List<Document> documents = documentService.getAllDocuments(organizationId);

        return ResponseEntity.ok(documents);
    }

    @Operation(summary = "Получение документа по ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Документ найден",
                            content = @Content(schema = @Schema(implementation = Document.class))),
                    @ApiResponse(responseCode = "404", description = "Документ не найден")
            })
    @GetMapping("/{id}")
    public ResponseEntity<?> getDocumentById(@RequestParam UUID organizationId, @PathVariable UUID id) {
        if (organizationId == null) {
            return ResponseEntity.badRequest().body("organizationId должен быть указан.");
        }
        Long aliasOrg = organizationService.getAlias(organizationId);
        if (aliasOrg == null) {
            return ResponseEntity.badRequest().body("Не удалось определить организацию, возможно вы не авторизованы");
        }

        Document document = documentService.getDocumentById(organizationId, id);

        if (document != null) {
            return ResponseEntity.ok(document);
        } else {
            return ResponseEntity.status(404).body("Документ не найден");
        }
    }

}
