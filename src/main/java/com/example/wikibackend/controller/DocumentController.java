package com.example.wikibackend.controller;

import com.example.wikibackend.dto.DocumentAccessDTO;
import com.example.wikibackend.dto.DocumentDTO;
import com.example.wikibackend.model.Document;
import com.example.wikibackend.service.DocumentService;
import com.example.wikibackend.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
                            examples = @ExampleObject(value = "{\"organizationId\": \"5929cf36-101f-471f-a9b0-afbb3964cd37\", \"title\": \"Заголовок документа\", \"status\": \"ACTIVE\",\"spaceId\": \"729f370d-faa8-486e-b4ee-bd46b70e14d6\", \"authorId\": \"79ddc8b8-1057-43e5-9e2b-f49fe9917a36\", \"parentId\": \"79ddc8b8-1057-43e5-9e2b-f49fe9917a36\", \"content\": \"Содержимое документа\"}"))),
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

        Document document = documentService.createDocument(documentDTO);

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
                            content = @Content(schema = @Schema(implementation = DocumentDTO.class))),
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

        DocumentDTO document = documentService.getDocumentById(organizationId, id);

        if (document != null) {
            return ResponseEntity.ok(document);
        } else {
            return ResponseEntity.status(404).body("Документ не найден");
        }
    }

    @Operation(summary = "Получение документов с доступом для пользователя",
            parameters = {
                    @Parameter(name = "userId", description = "ID пользователя", required = true, schema = @Schema(type = "string", format = "uuid")),
                    @Parameter(name = "organizationId", description = "ID организации", required = true, schema = @Schema(type = "string", format = "uuid"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список документов с их типами доступа", content = @Content(schema = @Schema(implementation = DocumentAccessDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Пользователь или организация не найдены")
            })
    @GetMapping("/user/{userId}/documents")
    public ResponseEntity<List<DocumentAccessDTO>> getDocumentsForUser(
            @PathVariable UUID userId,
            @RequestParam UUID organizationId) {

        // Проверка наличия организации
        Long aliasOrg = organizationService.getAlias(organizationId);
        if (aliasOrg == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // Получение документов с учетом доступа
        List<DocumentAccessDTO> accessibleDocuments = documentService.getDocumentsForUserWithAccess(userId, organizationId);

        return ResponseEntity.ok(accessibleDocuments);

        // пример ответа API
      /*  [
        {
            "document": {
            "id": "79ddc8b8-1057-43e5-9e2b-f49fe9917a36",
                    "title": "Документ 1",
                    "status": "ACTIVE",
                    "authorId": "f49fe9917a36",
                    "spaceId": "e4bdb4e0-bd46",
                    "parentId": null,
                    "createdAt": "2023-12-01T12:00:00",
                    "lastModifiedAt": "2023-12-15T12:00:00"
        },
            "accessType": "READ"
        },
        {
            "document": {
            "id": "23ddc8b8-1057-43e5-9e2b-f49fe9917b23",
                    "title": "Документ 2",
                    "status": "ACTIVE",
                    "authorId": "a49fe9917a36",
                    "spaceId": "b4bdb4e0-bd46",
                    "parentId": null,
                    "createdAt": "2023-12-01T12:00:00",
                    "lastModifiedAt": "2023-12-15T12:00:00"
        },
            "accessType": "FULL_ACCESS"
        }*/
]

        //---------------
    }


}
