package com.example.wikibackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                // Разрешить доступ без авторизации к Swagger UI и OpenAPI эндпойнтам
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // Все остальные запросы должны быть аутентифицированы
                .anyRequest().authenticated()
                .and()
                // Отключаем CSRF для простоты, пока не настроена аутентификация
                .csrf().disable();

        return http.build();
    }
}
