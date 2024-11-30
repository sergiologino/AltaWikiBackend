package com.example.wikibackend.controller;

import com.example.wikibackend.dto.UserRoleDTO;
import com.example.wikibackend.service.OrganizationService;
import com.example.wikibackend.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    private final UserRoleService userRoleService;
    private final OrganizationService organizationService;

    @Autowired
    public UserRoleController(UserRoleService userRoleService, OrganizationService organizationService) {
        this.userRoleService = userRoleService;
        this.organizationService = organizationService;
    }

    @Operation(summary = "Присвоить роль пользователю",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRoleDTO.class),
                            examples = @ExampleObject(value = "{\"organizationId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"userId\": \"79ddc8b8-1057-43e5-9e2b-f49fe9917a36\", \"roleId\": \"d9aab576-46db-48e4-85db-2167a683ee3f\"}"))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Роль успешно присвоена пользователю"),
                    @ApiResponse(responseCode = "404", description = "Пользователь или роль не найдены")
            })
    @PostMapping("/assign")
    public ResponseEntity<String> assignRoleToUser(@RequestBody UserRoleDTO userRoleDTO) {

        Long aliasOrg = organizationService.getAlias(userRoleDTO.getOrganizationId());
        if (aliasOrg == null) {
            System.out.println("Не удалось получить организацию, проверьте авторизацию");
            return ResponseEntity.badRequest().build();
        }

        boolean isAssigned = userRoleService.assignRoleToUser(userRoleDTO.getUserId(), userRoleDTO.getRoleId());
        if (isAssigned) {
            return ResponseEntity.ok("Роль успешно присвоена пользователю");
        } else {
            return ResponseEntity.status(404).body("Пользователь или роль не найдены");
        }
    }

    @Operation(summary = "Удалить роль у пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRoleDTO.class),
                            examples = @ExampleObject(value = "{\"userId\": 1, \"roleId\": 2}"))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Роль успешно удалена у пользователя"),
                    @ApiResponse(responseCode = "404", description = "Пользователь или роль не найдены")
            })
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeRoleFromUser(@RequestBody UserRoleDTO userRoleDTO) {
        boolean isRemoved = userRoleService.removeRoleFromUser(userRoleDTO.getUserId(), userRoleDTO.getRoleId());
        if (isRemoved) {
            return ResponseEntity.ok("Роль успешно удалена у пользователя");
        } else {
            return ResponseEntity.status(404).body("Пользователь или роль не найдены");
        }
    }

    @Operation(summary = "Изменить роль пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRoleDTO.class),
                            examples = @ExampleObject(value = "{\"userId\": 1, \"roleId\": 3}"))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Роль пользователя успешно изменена"),
                    @ApiResponse(responseCode = "404", description = "Пользователь или роль не найдены")
            })
    @PutMapping("/change")
    public ResponseEntity<String> changeUserRole(@RequestBody UserRoleDTO userRoleDTO) {
        boolean isChanged = userRoleService.changeUserRole(userRoleDTO.getUserId(), userRoleDTO.getRoleId());
        if (isChanged) {
            return ResponseEntity.ok("Роль пользователя успешно изменена");
        } else {
            return ResponseEntity.status(404).body("Пользователь или роль не найдены");
        }
    }
}
