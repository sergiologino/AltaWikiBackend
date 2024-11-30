package com.example.wikibackend.config;

import com.example.wikibackend.service.SchemaService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SchemaAspect {

    private final SchemaService schemaService;

    public SchemaAspect(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @Around("@annotation(SwitchSchema) && args(organizationId,..)")
    public Object switchSchema(ProceedingJoinPoint joinPoint, Long organizationId) throws Throwable {
        if (organizationId == null) {
            String schema = "admin";
            SchemaContext.setSchema(schema);

        } else {
        String schema = "alt_" + schemaService.getSchemaByOrganizationId(organizationId);
              SchemaContext.setSchema(schema);


        }
        try {
            return joinPoint.proceed();
        } finally {
            SchemaContext.clearSchema();
        }
    }

}