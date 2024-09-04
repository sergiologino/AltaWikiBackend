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

        String schemaName = "alt_" + organizationId + "_data";
        createSchema(schemaName);
        copySchemaStructure("template_schema", schemaName);
    }

    private void createSchema(String schemaName) {
        String createSchemaSql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
        jdbcTemplate.execute(createSchemaSql);
    }
    private void copySchemaStructure(String sourceSchema, String targetSchema) {
        // Копирование таблиц с ограничениями
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

//        // Обновленный SQL для копирования индексов
//        String copyIndexesSql = String.format(
//                "DO $$ DECLARE " +
//                        "  index_record RECORD; " +
//                        "BEGIN " +
//                        "  FOR index_record IN " +
//                        "    (SELECT c2.relname AS indexname, pg_get_indexdef(i.indexrelid) AS indexdef " +
//                        "     FROM pg_class c " +
//                        "     JOIN pg_index i ON c.oid = i.indrelid " +
//                        "     JOIN pg_class c2 ON i.indexrelid = c2.oid " +
//                        "     WHERE c.relnamespace = (SELECT oid FROM pg_namespace WHERE nspname = '%s')) LOOP " +
//                        "    EXECUTE 'CREATE INDEX ' || index_record.indexname || '_%s ON %s.' || replace(index_record.indexdef, 'CREATE INDEX ' || index_record.indexname, ''); " +
//                        "  END LOOP; " +
//                        "END $$;",
//                sourceSchema, targetSchema, targetSchema
//        );
//        jdbcTemplate.execute(copyIndexesSql);

        // Копирование ограничений (primary key, foreign key, unique)
        String copyConstraintsSql = String.format(
                "DO $$ DECLARE " +
                        "  constraint_record RECORD; " +
                        "BEGIN " +
                        "  FOR constraint_record IN " +
                        "    (SELECT conname, pg_get_constraintdef(oid) AS constdef, conrelid::regclass AS table_name " +
                        "     FROM pg_constraint WHERE connamespace = (SELECT oid FROM pg_namespace WHERE nspname = '%s')) LOOP " +
                        "    EXECUTE 'ALTER TABLE %s.' || constraint_record.table_name || ' ADD CONSTRAINT ' || constraint_record.conname || ' ' || constraint_record.constdef; " +
                        "  END LOOP; " +
                        "END $$;",
                sourceSchema, targetSchema
        );
        jdbcTemplate.execute(copyConstraintsSql);
    }


//    private void copySchemaStructure(String sourceSchema, String targetSchema) {
//        String copyTablesSql = String.format(
//                "DO $$ DECLARE " +
//                        "  table_record RECORD; " +
//                        "BEGIN " +
//                        "  FOR table_record IN SELECT table_name FROM information_schema.tables WHERE table_schema = '%s' LOOP " +
//                        "    EXECUTE 'CREATE TABLE %s.' || table_record.table_name || ' (LIKE %s.' || table_record.table_name || ' INCLUDING DEFAULTS)'; " +
//                        "  END LOOP; " +
//                        "END $$;",
//                sourceSchema, targetSchema, sourceSchema
//        );
//        jdbcTemplate.execute(copyTablesSql);
//    }
    //    private void copySchemaStructure(String sourceSchema, String targetSchema) {
//        // Копирование таблиц
//        String copyTablesSql = String.format(
//                "DO $$ DECLARE " +
//                        "  table_record RECORD; " +
//                        "BEGIN " +
//                        "  FOR table_record IN SELECT table_name FROM information_schema.tables WHERE table_schema = '%s' LOOP " +
//                        "    EXECUTE 'CREATE TABLE %s.' || table_record.table_name || ' (LIKE %s.' || table_record.table_name || ' INCLUDING ALL)'; " +
//                        "  END LOOP; " +
//                        "END $$;",
//                sourceSchema, targetSchema, sourceSchema
//        );
//        jdbcTemplate.execute(copyTablesSql);
//
//        // Исправленный SQL для копирования индексов
//        String copyIndexesSql = String.format(
//                "DO $$ DECLARE " +
//                        "  index_record RECORD; " +
//                        "BEGIN " +
//                        "  FOR index_record IN " +
//                        "    (SELECT c2.relname AS indexname, pg_get_indexdef(i.indexrelid) AS indexdef " +
//                        "     FROM pg_class c " +
//                        "     JOIN pg_index i ON c.oid = i.indrelid " +
//                        "     JOIN pg_class c2 ON i.indexrelid = c2.oid " +
//                        "     WHERE c.relkind = 'r' " +
//                        "     AND c.relnamespace = (SELECT oid FROM pg_namespace WHERE nspname = '%s')) LOOP " +
//                        "    EXECUTE replace(index_record.indexdef, 'CREATE INDEX', 'CREATE INDEX IF NOT EXISTS ' || index_record.indexname || '_%s') " +
//                        "    || ' ON %s.' || replace(substring(index_record.indexdef FROM 'ON [^ ]+'), 'ON ', '') || ';'; " +
//                        "  END LOOP; " +
//                        "END $$;",
//                sourceSchema, targetSchema, targetSchema
//        );
//        jdbcTemplate.execute(copyIndexesSql);
//    }

}
