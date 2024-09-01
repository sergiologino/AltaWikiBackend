package com.example.wikibackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrganizationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void registerOrganization(String organizationName) {
        String insertOrganizationSql = "INSERT INTO admin.organizations (name) VALUES (?) RETURNING id";
        Long organizationId = jdbcTemplate.queryForObject(insertOrganizationSql, new Object[]{organizationName}, Long.class);

        String schemaName = "alt_" + organizationId + "_schema";
        createSchema(schemaName);
        copySchemaStructure("template_schema", schemaName);
    }

    private void createSchema(String schemaName) {
        String createSchemaSql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
        jdbcTemplate.execute(createSchemaSql);
    }

    private void copySchemaStructure(String sourceSchema, String targetSchema) {
        // Копирование таблиц, как и прежде
        String copyTablesSql = String.format(
                "DO $$ DECLARE " +
                        "  table_record RECORD; " +
                        "BEGIN " +
                        "  FOR table_record IN SELECT table_name FROM information_schema.tables WHERE table_schema = '%s' LOOP " +
                        "    EXECUTE 'CREATE TABLE %s.' || table_record.table_name || ' (LIKE %s.' || table_record.table_name || ' INCLUDING ALL)'; " +
                        "  END LOOP; " +
                        "END $$;",
                sourceSchema, targetSchema, sourceSchema
        );
        jdbcTemplate.execute(copyTablesSql);

        // Исправленный SQL для копирования индексов
        String copyIndexesSql = String.format(
                "DO $$ DECLARE " +
                        "  index_record RECORD; " +
                        "BEGIN " +
                        "  FOR index_record IN SELECT indexname, indexdef FROM pg_indexes WHERE schemaname = '%s' LOOP " +
                        "    EXECUTE 'CREATE INDEX ' || index_record.indexname || '_%s' || " +
                        "    substring(indexdef from position(' ON ' in indexdef)) " +
                        "    || ' ON %s.' || split_part(indexdef, ' ON %s.', 2); " +
                        "  END LOOP; " +
                        "END $$;",
                sourceSchema, targetSchema, targetSchema, sourceSchema
        );
        jdbcTemplate.execute(copyIndexesSql);
    }
}
