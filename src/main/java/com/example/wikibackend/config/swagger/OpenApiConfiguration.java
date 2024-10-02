package com.example.wikibackend.config.swagger;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

public class OpenApiConfiguration {
    public OpenAPI openApiDescription() {
        Server localhostServer = new Server();
        localhostServer.setUrl("http://localhost:8080");
        localhostServer.setDescription("local environment");

        Server productionServer = new Server();
        productionServer.setUrl("https://altawiki.altacod.ru");
        productionServer.setDescription("Production environment");

        Contact contact = new Contact();
        contact.setName("Савкин Сергей");
        contact.setEmail("savkin@altacod.ru");
        contact.setUrl("https://altacod.ru");

        License mitLicense = new License().name("GNU AGPLv3")
                .url("https://choosealicense.com/license/agpl-3.0/");

        Info info = new Info()
                .title("API. Wiki документация для IT команд")
                .version("1.0")
                .contact(contact)
                .description("API для ведения базы знаний")
                .termsOfService("https://terms-altawiki.altacod.ru")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(localhostServer, productionServer));
    }
}

