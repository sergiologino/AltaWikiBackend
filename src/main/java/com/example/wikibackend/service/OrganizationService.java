package com.example.wikibackend.service;

import com.example.wikibackend.mapper.OrganizationMapper;
import com.example.wikibackend.model.Organization;
import com.example.wikibackend.repository.OrganizationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {


    private final JdbcTemplate jdbcTemplate;
    private final OrganizationRepository organizationRepository;



    @Autowired
    public OrganizationService(JdbcTemplate jdbcTemplate, OrganizationRepository organizationRepository){
        this.jdbcTemplate = jdbcTemplate;
        this.organizationRepository = organizationRepository;
    }

    @Transactional
    public Organization registerOrganization(Organization organization) {
        String name = organization.getName();
        Organization newOrg=organizationRepository.save(organization);
        Long orgPrefix = newOrg.getAlias();
//        String insertOrganizationSql = "INSERT INTO admin.organizations (name) VALUES (?) RETURNING alias";
//        String prefix = jdbcTemplate.queryForObject(insertOrganizationSql, new Object[]{name}, String.class);

        String schemaName = "alt_" + orgPrefix;
        createSchema(schemaName);
        //copySchemaStructure("template_schema", schemaName);
        cloneSchema(schemaName);
        return organization;
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


    // Метод для получения alias по UUID organizationId в схеме admin
    public Long getAlias(UUID organizationId) {
        // Ищем организацию по ID
        Optional<Organization> organizationOptional = organizationRepository.findById(organizationId);
        // Если организация найдена, возвращаем alias, иначе выбрасываем исключение
        if (organizationOptional.isPresent()) {
            return organizationOptional.get().getAlias(); // Предполагаем, что в Organization есть поле alias
        } else {
            throw new RuntimeException("Организация с ID " + organizationId + " не найдена.");
        }
    }
}
