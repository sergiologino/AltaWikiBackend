package com.example.wikibackend.controller;

import com.example.wikibackend.config.TenantContext;
import com.example.wikibackend.dto.RoleDTO;
import com.example.wikibackend.model.Role;
import com.example.wikibackend.service.OrganizationService;
import com.example.wikibackend.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    private final OrganizationService organizationService;

    @Autowired
    public RoleController(RoleService roleService, OrganizationService organizationService) {
        this.roleService = roleService;
        this.organizationService = organizationService;
    }

    @Operation(
            summary = "Получить все роли",
            parameters = {
                    @Parameter(
                            name = "organizationId",
                            description = "UUID организации",
                            required = true,
                            schema = @Schema(type = "string", format = "uuid")
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех ролей",
                            content = @Content(schema = @Schema(implementation = Role.class)))
            })
    @GetMapping("/{organizationId}")
    public ResponseEntity<List<Role>> getAllRoles(@PathVariable UUID organizationId) {
        Long aliasOrg = organizationService.getAlias(organizationId);
        if (aliasOrg == null) {
            return ResponseEntity.badRequest().build();
        }
        TenantContext.setCurrentTenant(aliasOrg);
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @Operation(summary = "Добавить новую роль",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleDTO.class),
                            examples = @ExampleObject(value = "{\"organizationId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"name\": \"Admin\"}"))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Роль успешно добавлена",
                            content = @Content(schema = @Schema(implementation = Role.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
            })
    @PostMapping
    public ResponseEntity<Role> addRole(@RequestBody RoleDTO roleDTO) {
        Long aliasOrg = organizationService.getAlias(roleDTO.getOrganizationId());
        if (aliasOrg == null) {
            return ResponseEntity.badRequest().build();
        }
        TenantContext.setCurrentTenant(aliasOrg);
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
        Long aliasOrg = organizationService.getAlias(roleDTO.getOrganizationId());
        if (aliasOrg == null) {
            return ResponseEntity.badRequest().build();
        }
        TenantContext.setCurrentTenant(aliasOrg);
        Role updatedRole = roleService.updateRole(id, roleDTO);
        return ResponseEntity.ok(updatedRole);
    }
}