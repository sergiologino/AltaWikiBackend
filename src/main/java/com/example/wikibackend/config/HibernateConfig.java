package com.example.wikibackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

//@Configuration
//public class HibernateConfig {
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
//        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
//        factoryBean.setDataSource(dataSource);
//        factoryBean.setPackagesToScan("com.example.wikibackend.model"); // Укажите пакет с вашими моделями
//        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//
//        // Настройки Hibernate
//        Properties properties = new Properties();
//        properties.put("hibernate.multiTenancy", "SCHEMA"); // Используем схему как источник многосхемного доступа
//        properties.put("hibernate.tenant_identifier_resolver", "com.example.TenantContextResolver");
//        properties.put("hibernate.multi_tenant_connection_provider", "com.example.TenantConnectionProvider");
//        factoryBean.setJpaProperties(properties);
//
//        return factoryBean;
//    }
//}
//
