package com.example.wikibackend.config;

public class TenantContext {

    private static final ThreadLocal<Long> currentTenant = new ThreadLocal<>();

    public static void setCurrentTenant(Long tenantId) {
        currentTenant.set(tenantId);
    }

    public static Long getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }
}

