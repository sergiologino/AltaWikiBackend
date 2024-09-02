package com.example.wikibackend.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Aspect
@Component
public class TenantSchemaInterceptor {

    private final JdbcTemplate jdbcTemplate;

    public TenantSchemaInterceptor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Before("execution(* com.example.wikibackend.service.*.*(..))")
    public void setSchemaForOrganization() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            Long organizationId = (Long) requestAttributes.getAttribute("organizationId", RequestAttributes.SCOPE_REQUEST);
            if (organizationId != null) {
                String schemaName = "alt_" + organizationId + "_data";
                jdbcTemplate.execute("SET search_path TO " + schemaName);
            }
        }
    }
}
