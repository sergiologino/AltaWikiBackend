package com.example.wikibackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy // включаем аспекты
public class WikiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikiBackendApplication.class, args);
    }

}
