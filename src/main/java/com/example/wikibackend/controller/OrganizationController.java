package com.example.wikibackend.controller;

import com.example.wikibackend.dto.OrganizationDTO;
import com.example.wikibackend.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @Operation(summary = "Регистрация новой организации",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Организация успешно зарегистрирована"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
            })
    @PostMapping("/register")
    public ResponseEntity<String> registerOrganization(@RequestBody OrganizationDTO organizationDTO) {
        organizationService.registerOrganization(organizationDTO.getOrganization());
        return ResponseEntity.ok("Организация успешно зарегистрирована");
    }
}

