package com.example.wikibackend.config;

 // Убедись, что пакет совпадает с указанным в настройках Hibernate

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

//@Component // Убедись, что класс зарегистрирован как Spring-бин
//public class TenantConnectionProvider implements MultiTenantConnectionProvider {
//
//    private final DataSource dataSource;
//
//    public TenantConnectionProvider(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    @Override
//    public Connection getConnection(String tenantIdentifier) throws SQLException {
//        Connection connection = dataSource.getConnection();
//        connection.createStatement().execute("SET search_path TO " + tenantIdentifier);
//        return connection;
//    }
//
//    @Override
//    public Connection getAnyConnection() throws SQLException {
//        return dataSource.getConnection();
//    }
//
//    @Override
//    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
//        connection.createStatement().execute("SET search_path TO public"); // Возвращаем схему по умолчанию
//        connection.close();
//    }
//
//    @Override
//    public void releaseAnyConnection(Connection connection) throws SQLException {
//        connection.close();
//    }
//
//    @Override
//    public Connection getConnection(Object o) throws SQLException {
//        return null;
//    }
//
//    @Override
//    public void releaseConnection(Object o, Connection connection) throws SQLException {
//
//    }
//
//    @Override
//    public boolean isUnwrappableAs(Class<?> unwrapType) {
//        return MultiTenantConnectionProvider.class.equals(unwrapType);
//    }
//
//    @Override
//    public <T> T unwrap(Class<T> unwrapType) {
//        return (T) this;
//    }
//
//    @Override
//    public boolean supportsAggressiveRelease() {
//        return false;
//    }
//}
