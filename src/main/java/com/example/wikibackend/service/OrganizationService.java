package com.example.wikibackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        //copySchemaStructure("template_schema", schemaName);
        cloneSchema(schemaName);
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
                sourceSchema, targetSchema, sourceSchema  // Передаем все необходимые аргументы для %s
        );
        jdbcTemplate.execute(copyTablesSql);

        // Обновленный SQL для копирования индексов с корректной заменой схемы
        String copyIndexesSql = String.format(
                "DO $$ DECLARE " +
                        "  index_record RECORD; " +
                        "BEGIN " +
                        "  FOR index_record IN " +
                        "    (SELECT c2.relname AS indexname, pg_get_indexdef(i.indexrelid) AS indexdef " +
                        "     FROM pg_class c " +
                        "     JOIN pg_index i ON c.oid = i.indrelid " +
                        "     JOIN pg_class c2 ON i.indexrelid = c2.oid " +
                        "     WHERE c.relnamespace = (SELECT oid FROM pg_namespace WHERE nspname = '%s')) LOOP " +
                        "    EXECUTE CASE " +
                        "      WHEN index_record.indexdef LIKE 'CREATE UNIQUE INDEX%%' THEN " +
                        "        'CREATE UNIQUE INDEX ' || index_record.indexname || '_%s ON %s.' || " +
                        "        replace(substring(index_record.indexdef FROM position(' ON ' IN index_record.indexdef) + 4), 'template_schema', '%s') " +
                        "      ELSE " +
                        "        'CREATE INDEX ' || index_record.indexname || '_%s ON %s.' || " +
                        "        replace(substring(index_record.indexdef FROM position(' ON ' IN index_record.indexdef) + 4), 'template_schema', '%s') " +
                        "    END; " +
                        "  END LOOP; " +
                        "END $$;",
                sourceSchema, targetSchema, targetSchema, targetSchema, targetSchema, sourceSchema, targetSchema  // Добавлены все аргументы для замены схем
        );
        jdbcTemplate.execute(copyIndexesSql);

        // Копирование ограничений (primary key, foreign key, unique)
        String copyConstraintsSql = String.format(
                "DO $$ DECLARE " +
                        "  constraint_record RECORD; " +
                        "BEGIN " +
                        "  FOR constraint_record IN " +
                        "    (SELECT conname, pg_get_constraintdef(oid) AS constdef, conrelid::regclass AS table_name " +
                        "     FROM pg_constraint WHERE connamespace = (SELECT oid FROM pg_namespace WHERE nspname = '%s')) LOOP " +
                        "    EXECUTE 'ALTER TABLE %s.' || constraint_record.table_name || ' ADD CONSTRAINT ' || constraint_record.conname || ' ' || " +
                        "    replace(constraint_record.constdef, 'REFERENCES %s.', 'REFERENCES %s.'); " +
                        "  END LOOP; " +
                        "END $$;",
                sourceSchema, targetSchema, sourceSchema, targetSchema  // Добавлены все аргументы для замены схем
        );
        jdbcTemplate.execute(copyConstraintsSql);
    }
    public void cloneSchema(String newSchemaName) {
        String sql = "DO LANGUAGE plpgsql $body$ "
                + "DECLARE "
                + "old_schema NAME = 'template_schema'; "
                + "new_schema NAME = '" + newSchemaName + "'; "
                + "tbl TEXT; "
                + "sql TEXT; "
                + "BEGIN "
                + "  EXECUTE format('CREATE SCHEMA IF NOT EXISTS %I', new_schema); "
                + "  FOR tbl IN "
                + "    SELECT table_name "
                + "    FROM information_schema.tables "
                + "    WHERE table_schema = old_schema "
                + "  LOOP "
                + "    sql := format('CREATE TABLE IF NOT EXISTS %I.%I (LIKE %I.%I INCLUDING INDEXES INCLUDING CONSTRAINTS)', new_schema, tbl, old_schema, tbl); "
                + "    EXECUTE sql; "
                + "  END LOOP; "
                + "END $body$;";

        jdbcTemplate.execute(sql);
    }



}
