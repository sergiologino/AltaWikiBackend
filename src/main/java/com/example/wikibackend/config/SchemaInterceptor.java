package com.example.wikibackend.config;

import org.hibernate.EmptyInterceptor;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SchemaInterceptor extends EmptyInterceptor implements StatementInspector {
    private static final Logger log = LoggerFactory.getLogger(SchemaInterceptor.class);
    private final SchemaContext schemaContext;

    @Autowired
    public SchemaInterceptor(SchemaContext schemaContext) {
        this.schemaContext = schemaContext;
    }

    @Override
    public String inspect(String sql) {
        log.info("-----зашли в SchemaInterceptor с переменной sql : {}", sql);
        String schema = schemaContext.getCurrentSchema();
        if (schema != null) {
            log.info("-----применяем схему: schema: {}", schema);
            String var = "SET search_path TO " + schema + ";" + sql;
            log.info("var");
            return var;
        }
        log.info("-----зашли в SchemaInterceptor но не получили схему, executing raw SQL.");
        return sql;
    }
}
