package com.example.wikibackend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SchemaContext {

    private static final ThreadLocal<String> currentSchema = new ThreadLocal<>();
    private static final Logger log = LoggerFactory.getLogger(SchemaContext.class);

    public static void setSchema(String schema) {
        currentSchema.set(schema);
        log.info("Зашли в SchemaContext переключили схему в " + schema);

        log.info("Теперь проверим переключение, установлена схема: " + currentSchema.get());
    }

    public String getCurrentSchema() {
        return currentSchema.get();
    }

    public static void clearSchema() {
        currentSchema.remove();
        log.info("Schema cleared");
    }
}
