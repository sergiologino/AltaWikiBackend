package com.example.wikibackend.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class TenantSchemaAspect {

    private final JdbcTemplate jdbcTemplate;

    public TenantSchemaAspect(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Before("execution(* com.example.wikibackend.service.*.*(..))")
    public void setSchemaForOrganization() {
        UUID organizationId = TenantContext.getCurrentTenant();
        if (organizationId != null) {
            String schemaName = "alt_" + organizationId + "_data";
            jdbcTemplate.execute("SET search_path TO " + schemaName);
        }
    }
}

