package com.example.wikibackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrganizationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void registerOrganization(String name) {
        String insertOrganizationSql = "INSERT INTO admin.organizations (name) VALUES (?) RETURNING alias";
        String prefix = jdbcTemplate.queryForObject(insertOrganizationSql, new Object[]{name}, String.class);

        String schemaName = "alt_" + prefix;
        createSchema(schemaName);
        //copySchemaStructure("template_schema", schemaName);
        cloneSchema(schemaName);
    }

    private void createSchema(String schemaName) {
        String createSchemaSql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
        jdbcTemplate.execute(createSchemaSql);
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


    public Long getAlias(Integer organizationId) {
        Long Alias = getAlias(organizationId);
        return Alias;
    }
}
