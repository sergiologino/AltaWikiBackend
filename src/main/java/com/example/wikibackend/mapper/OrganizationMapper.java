package com.example.wikibackend.mapper;

import com.example.wikibackend.dto.OrganizationDTO;
import com.example.wikibackend.model.Organization;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {

    // Преобразование DTO в сущность Organization
    public Organization toEntity(OrganizationDTO organizationDTO) {
        Organization organization = new Organization();
        //organization.set(organizationDTO.getId());
        organization.setName(organizationDTO.getOrganization());
        organization.setAlias(organizationDTO.getAlias());
        return organization;
    }

    // Преобразование сущности Organization в DTO
    public OrganizationDTO toDTO(Organization organization) {
        OrganizationDTO newOrg =new OrganizationDTO();
        newOrg.setOrganization(organization.getName());
        newOrg.setAlias(organization.getAlias());
        return newOrg;
    }
}
