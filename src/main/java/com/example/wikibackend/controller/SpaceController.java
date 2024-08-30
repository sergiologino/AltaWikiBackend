package com.example.wikibackend.controller;

import com.example.wikibackend.dto.SpaceDTO;
import com.example.wikibackend.model.Space;
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

@RestController
@RequestMapping("/api/spaces")
public class SpaceController {

    private final SpaceService spaceService;

    @Autowired
    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @Operation(summary = "Получить все разделы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех разделов",
                            content = @Content(schema = @Schema(implementation = Space.class)))
            })
    @GetMapping
    public ResponseEntity<List<Space>> getAllSpaces() {
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
    public ResponseEntity<Space> updateSpace(@PathVariable Long id, @RequestBody SpaceDTO spaceDTO) {
        Space updatedSpace = spaceService.updateSpace(id, spaceDTO);
        return ResponseEntity.ok(updatedSpace);
    }
}

