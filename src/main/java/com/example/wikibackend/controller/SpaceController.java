package com.example.wikibackend.controller;

import com.example.wikibackend.dto.SpaceDTO;
import com.example.wikibackend.model.Space;
import com.example.wikibackend.service.OrganizationService;
import com.example.wikibackend.service.SpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
            parameters = {
                    @Parameter(
                            name = "organizationId",
                            description = "UUID организации",
                            required = true,
                            schema = @Schema(type = "string", format = "uuid")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех разделов",
                            content = @Content(schema = @Schema(implementation = Space.class)))
            })
    @GetMapping("/organizationId")
    public ResponseEntity<List<Space>> getAllSpaces(@PathVariable UUID organizationId) {
        Long aliasOrg = organizationService.getAlias(organizationId);
        if (aliasOrg == null) {
            System.out.println("Не удалось получить организацию, проверьте авторизацию");
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(spaceService.getAllSpaces(organizationId));
    }

    @Operation(summary = "Добавить новый раздел",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceDTO.class),
                            examples = @ExampleObject(value = "{\"organizationId\": \"5929cf36-101f-471f-a9b0-afbb3964cd37\",\"name\": \"Новый раздел\", \"description\": \"Описание раздела\",\"authorId\": \"id автора (текущий пользователь)\"}"))),
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

        Space space = spaceService.addSpace(spaceDTO);
        return ResponseEntity.status(201).body(space);
    }

    @Operation(summary = "Изменить раздел",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(

                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceDTO.class),
                            examples = @ExampleObject(value = "{\"organizationId\": \"5929cf36-101f-471f-a9b0-afbb3964cd37\",\"name\": \"Обновленный раздел\",  \"description\": \"Описание раздела\",\"authorId\": \"id автора (текущий пользователь)\"}"))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Раздел успешно изменен",
                            content = @Content(schema = @Schema(implementation = Space.class))),
                    @ApiResponse(responseCode = "404", description = "Раздел не найден")
            })
    @PutMapping("/{Spaceid}")
    public ResponseEntity<Space> updateSpace(@PathVariable UUID Spaceid, @RequestBody SpaceDTO spaceDTO) {
        Long aliasOrg = organizationService.getAlias(spaceDTO.getOrganizationId());
        if (aliasOrg == null) {
            System.out.println("Не удалось получить организацию, проверьте авторизацию");
            return ResponseEntity.badRequest().build();
        }

        Space updatedSpace = spaceService.updateSpace(Spaceid, spaceDTO);
        return ResponseEntity.ok(updatedSpace);
    }

    @GetMapping("/{organizationId}{id}")
    public ResponseEntity<SpaceDTO> getSpace(@PathVariable UUID organizationId, @PathVariable UUID id) {
        Long aliasOrg = organizationService.getAlias(organizationId);
        if (aliasOrg == null) {
            System.out.println("Не удалось получить организацию, проверьте авторизацию");
            return ResponseEntity.badRequest().build();
        }
        SpaceDTO spaceDTO = spaceService.findSpaceById(organizationId, id);
        return ResponseEntity.ok(spaceDTO);
    }
}

