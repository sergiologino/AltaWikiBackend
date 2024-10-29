package com.example.wikibackend.controller;

import com.example.wikibackend.dto.OrganizationDTO;
import com.example.wikibackend.mapper.OrganizationMapper;
import com.example.wikibackend.model.Organization;
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
    public ResponseEntity<Organization> registerOrganization(@RequestBody OrganizationDTO organizationDTO) {
        OrganizationMapper newOrgMap = new OrganizationMapper();
        Organization entityOrg= newOrgMap.toEntity(organizationDTO);
        Organization createdOrganization = organizationService.registerOrganization(entityOrg);
        return ResponseEntity.ok(createdOrganization);
    }
}

