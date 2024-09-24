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

    @Before("execution(* com.example.wikibackend..*(..)) && @annotation(com.example.wikibackend.config.SwitchSchema)")

    public void switchSchema() {
        Long alias = TenantContext.getCurrentTenant();
        if (alias != null) {
            String schemaName = "alt_" + alias;
            jdbcTemplate.execute("SET search_path TO '" + schemaName+ "'");
//            jdbcTemplate.execute("SET SCHEMA ' " + schemaName+ "'");
            System.out.println("Переключено на схему: "+ schemaName);
            Boolean bol = true;
        }
    }
}

