package com.example.wikibackend.config;


import com.example.wikibackend.model.Organization;
import com.example.wikibackend.repository.OrganizationRepository;
import com.example.wikibackend.service.OrganizationService;

import java.util.UUID;

public class TenantContext {

    private static final ThreadLocal<Long> currentTenant = new ThreadLocal<>();

    public TenantContext() {

    }

    public static void setCurrentTenant(UUID organizationId) {
        Organization currentOrganization = OrganizationService.getOrganizationById(organizationId);
        currentTenant.set(currentOrganization.getAlias());
    }

    public static Long getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }
}

