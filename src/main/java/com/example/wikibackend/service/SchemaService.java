package com.example.wikibackend.service;

import com.example.wikibackend.config.SchemaContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class SchemaService {

    private static final Logger log = LoggerFactory.getLogger(SchemaService.class);
    private final JdbcTemplate jdbcTemplate;
    private final SchemaContext schemaContext;
    private final OrganizationService organizationService;

    @PersistenceContext
    private EntityManager entityManager;

    public SchemaService(JdbcTemplate jdbcTemplate, SchemaContext schemaContext, OrganizationService organizationService) {
        this.jdbcTemplate = jdbcTemplate;
        this.schemaContext = schemaContext;
        this.organizationService = organizationService;


    }

    public String getSchemaByOrganizationId(Long id) {
        String query = "SELECT alias FROM admin.organizations WHERE id = ?";
        String currentAlias=jdbcTemplate.queryForObject(query, new Object[]{id}, String.class);

        System.out.println("schema service set alias: "+currentAlias);
        return currentAlias;
    }
    public String getSchemaByOrganizationName(String name){
        String query = "SELECT alias FROM admin.organizations WHERE name = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{name}, String.class);
    }
    public void setSchema(UUID organizationId) {
        Long aliasOrg = organizationService.getAlias(organizationId);
        String schema="alt_"+aliasOrg;;
        SchemaContext.setSchema(schema);
        entityManager.createNativeQuery("SET search_path TO " + schema).executeUpdate();
        log.info("set current Schema: "+schemaContext.getCurrentSchema());
    }

}
