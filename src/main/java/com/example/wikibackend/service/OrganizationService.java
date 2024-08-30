package com.example.wikibackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrganizationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Метод для регистрации новой организации
    public void registerOrganization(String organizationName) {
        // Вставка новой организации и получение её ID из схемы admin
        String insertOrganizationSql = "INSERT INTO admin.organizations (name) VALUES (?) RETURNING id";
        Long organizationId = jdbcTemplate.queryForObject(insertOrganizationSql, new Object[]{organizationName}, Long.class);

        // Создание схемы для новой организации
        String schemaName = "o" + organizationId + "_schema";
        createSchema(schemaName);

        // Копирование структуры данных из шаблонной схемы
        copySchemaStructure("template_schema", schemaName);
    }

    // Метод для создания новой схемы
    private void createSchema(String schemaName) {
        String createSchemaSql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
        jdbcTemplate.execute(createSchemaSql);
    }

    // Метод для копирования структуры данных из одной схемы в другую
    private void copySchemaStructure(String sourceSchema, String targetSchema) {
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

        String copyIndexesSql = String.format(
                "DO $$ DECLARE " +
                        "  index_record RECORD; " +
                        "BEGIN " +
                        "  FOR index_record IN SELECT indexname, indexdef FROM pg_indexes WHERE schemaname = '%s' LOOP " +
                        "    EXECUTE replace(index_record.indexdef, '%s', '%s'); " +
                        "  END LOOP; " +
                        "END $$;",
                sourceSchema, sourceSchema, targetSchema
        );

        jdbcTemplate.execute(copyIndexesSql);
    }
}
