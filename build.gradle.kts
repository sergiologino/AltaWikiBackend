//jdk.tools.jlink.resources.
plugins {
    java
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

//java {
//    toolchain {
//        languageVersion = JavaLanguageVersion.of(17)
//    }
//}
java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2023.0.3"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-core
    implementation("org.hibernate.orm:hibernate-core:6.6.2.Final")//5.6.15.Final
    //implementation("org.hibernate.orm:hibernate-core")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0"){
   //     exclude(group="io.swagger.core.v3", module="swagger-core")
    }
//    implementation("io.swagger.core.v3:swagger-core:2.2.23")
//    implementation("io.swagger.core.v3:swagger-annotations:2.2.23")
 //   implementation("org.openapitools:openapi-generator-gradle-plugin:7.0.1")
//    { exclude (group="org.slf4j", module="slf4j-simple")}
    implementation("jakarta.validation:jakarta.validation-api")
    implementation("junit:junit:4.13.1")
    implementation("junit:junit:4.13.1")
    implementation("io.micrometer:micrometer-registry-prometheus")
    // Spring Boot Actuator for metrics
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Logback for JSON logging
    implementation("net.logstash.logback:logstash-logback-encoder:6.6")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5") // Для работы с JSON через Jackson

    compileOnly("org.projectlombok:lombok")
    runtimeOnly ("org.postgresql:postgresql")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}



dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
