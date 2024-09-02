# Используем официальный образ OpenJDK версии 17
FROM openjdk:17-jdk-slim

# Создаем том для временных файлов
VOLUME /tmp

# Объявляем переменную для JAR файла, которую можно изменить при сборке
ARG JAR_FILE=target/auth-service-0.0.1-SNAPSHOT.jar

# Копируем JAR файл в контейнер
COPY ${JAR_FILE} app.jar

# Устанавливаем точку входа для запуска JAR файла
ENTRYPOINT ["java","-jar","/app.jar"]

