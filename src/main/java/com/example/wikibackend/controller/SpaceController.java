package com.example.wikibackend.controller;

import com.example.wikibackend.config.TenantContext;
import com.example.wikibackend.dto.SpaceDTO;
import com.example.wikibackend.model.Space;
import com.example.wikibackend.service.OrganizationService;
import com.example.wikibackend.service.SpaceService;
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
@RequestMapping("/api/spaces")
public class SpaceController {

    private final SpaceService spaceService;
    private final OrganizationService organizationService;

    @Autowired
    public SpaceController(SpaceService spaceService, OrganizationService organizationService) {
        this.spaceService = spaceService;
        this.organizationService = organizationService;
    }

    @Operation(summary = "Получить все разделы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех разделов",
                            content = @Content(schema = @Schema(implementation = Space.class)))
            })
    @GetMapping
    public ResponseEntity<List<Space>> getAllSpaces(@PathVariable UUID organizationId) {
        Long aliasOrg = organizationService.getAlias(organizationId);
        if (aliasOrg == null) {
            System.out.println("Не удалось получить организацию, проверьте авторизацию");
            return ResponseEntity.badRequest().build();
        }
        TenantContext.setCurrentTenant(aliasOrg);
        return ResponseEntity.ok(spaceService.getAllSpaces());
    }

    @Operation(summary = "Добавить новый раздел",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceDTO.class),
                            examples = @ExampleObject(value = "{\"name\": \"Новый раздел\", \"author\": \"Автор\"}"))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Раздел успешно добавлен",
                            content = @Content(schema = @Schema(implementation = Space.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
            })
    @PostMapping
    public ResponseEntity<Space> addSpace(@RequestBody SpaceDTO spaceDTO) {
        Long aliasOrg = organizationService.getAlias(spaceDTO.getOrganizationId());
        if (aliasOrg == null) {
            System.out.println("Не удалось получить организацию, проверьте авторизацию");
            return ResponseEntity.badRequest().build();
        }
        TenantContext.setCurrentTenant(aliasOrg);
        Space space = spaceService.addSpace(spaceDTO);
        return ResponseEntity.status(201).body(space);
    }

    @Operation(summary = "Изменить раздел",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceDTO.class),
                            examples = @ExampleObject(value = "{\"name\": \"Обновленный раздел\", \"author\": \"Автор\"}"))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Раздел успешно изменен",
                            content = @Content(schema = @Schema(implementation = Space.class))),
                    @ApiResponse(responseCode = "404", description = "Раздел не найден")
            })
    @PutMapping("/{id}")
    public ResponseEntity<Space> updateSpace(@PathVariable UUID id, @RequestBody SpaceDTO spaceDTO) {
        Long aliasOrg = organizationService.getAlias(spaceDTO.getOrganizationId());
        if (aliasOrg == null) {
            System.out.println("Не удалось получить организацию, проверьте авторизацию");
            return ResponseEntity.badRequest().build();
        }
        TenantContext.setCurrentTenant(aliasOrg);
        Space updatedSpace = spaceService.updateSpace(id, spaceDTO);
        return ResponseEntity.ok(updatedSpace);
    }
}

