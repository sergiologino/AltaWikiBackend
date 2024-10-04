package com.example.wikibackend.controller;

import com.example.wikibackend.config.TenantContext;
import com.example.wikibackend.dto.UserSpaceAccessDTO;
import com.example.wikibackend.model.Space;
import com.example.wikibackend.model.UserSpaceAccess;
import com.example.wikibackend.service.OrganizationService;
import com.example.wikibackend.service.UserSpaceAccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/access")
public class UserSpaceAccessController {

    private final UserSpaceAccessService userSpaceAccessService;
    private final OrganizationService organizationService;

    @Autowired
    public UserSpaceAccessController(UserSpaceAccessService userSpaceAccessService, OrganizationService organizationService) {
        this.userSpaceAccessService = userSpaceAccessService;

        this.organizationService = organizationService;
    }

    @Operation(summary = "Получение полного списка разделов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное получение списка разделов",
                            content = @Content(schema = @Schema(implementation = Space.class)))
            })
    @GetMapping("/spaces")
    public ResponseEntity<List<Space>> getAllSpaces() {
        return ResponseEntity.ok(userSpaceAccessService.getAllSpaces());
    }

    @Operation(summary = "Назначение пользователю списка доступных разделов",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSpaceAccessDTO.class),
                            examples = @ExampleObject(value = "{\"organizationId\": \"id\",\"userId\": \"id\", \"spaceId\": \"id\", \"accessType\": \"READ or DENIED or FULL_ACCESS\"}"))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное назначение доступа",
                            content = @Content(schema = @Schema(implementation = UserSpaceAccess.class)))
            })
    @PostMapping("/assign")
    public ResponseEntity<UserSpaceAccess> assignSpaceAccess(@RequestBody UserSpaceAccessDTO userSpaceAccessDTO) {
        Long aliasOrg = organizationService.getAlias(userSpaceAccessDTO.getOrganizationId());
        TenantContext.setCurrentTenant(aliasOrg);
        System.out.println("Current tenant in controller: "+TenantContext.getCurrentTenant());
        return ResponseEntity.ok(userSpaceAccessService.assignSpaceAccess(userSpaceAccessDTO.getUserId(),userSpaceAccessDTO.getSpaceId(),userSpaceAccessDTO.getAccessType()));
    }

    @Operation(summary = "Получение по пользователю списка доступных ему разделов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное получение списка разделов",
                            content = @Content(schema = @Schema(implementation = UserSpaceAccess.class)))
            })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserSpaceAccess>> getUserSpaceAccesses(@PathVariable UUID organizationId, UUID userId) {
        Long aliasOrg = organizationService.getAlias(organizationId);
        TenantContext.setCurrentTenant(aliasOrg);
        return ResponseEntity.ok(userSpaceAccessService.getUserSpaceAccesses(userId));
    }

    @Operation(summary = "Получение по ID раздела списка пользователей, которым он доступен",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное получение списка пользователей",
                            content = @Content(schema = @Schema(implementation = UserSpaceAccess.class)))
            })
    @GetMapping("/space/{spaceId}")
    public ResponseEntity<List<UserSpaceAccess>> getSpaceUsers(@PathVariable UUID organizationId, UUID spaceId) {
        Long aliasOrg = organizationService.getAlias(organizationId);
        TenantContext.setCurrentTenant(aliasOrg);
        return ResponseEntity.ok(userSpaceAccessService.getSpaceUsers(spaceId));
    }
}

