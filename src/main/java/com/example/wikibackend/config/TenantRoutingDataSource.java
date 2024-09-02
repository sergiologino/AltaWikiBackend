package com.example.wikibackend.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        // Возвращаем текущий organizationId из контекста
        return TenantContext.getCurrentTenant();
    }
}
