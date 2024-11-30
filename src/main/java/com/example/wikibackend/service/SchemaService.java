package com.example.wikibackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SchemaService {

    private static final Logger log = LoggerFactory.getLogger(SchemaService.class);
    private final JdbcTemplate jdbcTemplate;

    public SchemaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

}
