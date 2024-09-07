package com.example.wikibackend.controller;

import com.example.wikibackend.dto.RoleDTO;
import com.example.wikibackend.model.Role;
import com.example.wikibackend.service.RoleService;
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
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "Получить все роли",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех ролей",
                            content = @Content(schema = @Schema(implementation = Role.class)))
            })
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @Operation(summary = "Добавить новую роль",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleDTO.class),
                            examples = @ExampleObject(value = "{\"name\": \"Admin\"}"))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Роль успешно добавлена",
                            content = @Content(schema = @Schema(implementation = Role.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
            })
    @PostMapping
    public ResponseEntity<Role> addRole(@RequestBody RoleDTO roleDTO) {
        Role role = roleService.addRole(roleDTO);
        return ResponseEntity.status(201).body(role);
    }

    @Operation(summary = "Изменить роль",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleDTO.class),
                            examples = @ExampleObject(value = "{\"name\": \"User\"}"))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Роль успешно изменена",
                            content = @Content(schema = @Schema(implementation = Role.class))),
                    @ApiResponse(responseCode = "404", description = "Роль не найдена")
            })
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable UUID id, @RequestBody RoleDTO roleDTO) {
        Role updatedRole = roleService.updateRole(id, roleDTO);
        return ResponseEntity.ok(updatedRole);
    }
}