package com.example.wikibackend.controller;

import com.example.wikibackend.config.TenantContext;
import com.example.wikibackend.dto.UserDTO;
import com.example.wikibackend.model.User;
import com.example.wikibackend.repository.OrganizationRepository;
import com.example.wikibackend.service.OrganizationService;
import com.example.wikibackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final OrganizationService organizationService;


    @Autowired
    public UserController(UserService userService, OrganizationService organizationService) {
        this.userService = userService;
        this.organizationService = organizationService;
    }

    @Operation(summary = "Создание нового пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class),
                            examples = @ExampleObject(value = "{\"organizationId\": 10, \"username\": \"Вася\", \"password\": \"123\", \"email\": \"sample@altacod.ru\"}"))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
                            content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
            })
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO) {
        // Устанавливаем organizationId в TenantContext
        Long aliasOrg = organizationService.getAlias(userDTO.getOrganizationId());
        TenantContext.setCurrentTenant(aliasOrg);
        System.out.println("Current tenant in controller: "+TenantContext.getCurrentTenant());
        User user = userService.addUser(userDTO);

        // Очищаем контекст после выполнения операции
        TenantContext.clear();
        return ResponseEntity.status(201).body(user);
    }

    @Operation(summary = "Авторизация пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class),
                            examples = @ExampleObject(value = "{\"username\": \"john_doe\", \"password\": \"password123\"}"))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Авторизация успешна"),
                    @ApiResponse(responseCode = "401", description = "Некорректные учетные данные")
            })
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO) {
        // Бизнес-логика авторизации пользователя
        boolean isAuthenticated = userService.authenticateUser(userDTO.getUsername(), userDTO.getPassword());
        if (isAuthenticated) {
            return ResponseEntity.ok("Авторизация успешна");
        } else {
            return ResponseEntity.status(401).body("Некорректные учетные данные");
        }
    }

    @Operation(summary = "Получение списка всех пользователей",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех пользователей",
                            content = @Content(schema = @Schema(implementation = User.class)))
            })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Удаление пользователя (установка признака 'deleted')",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь успешно помечен как удаленный"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.ok("Пользователь успешно помечен как удаленный");
        } else {
            return ResponseEntity.status(404).body("Пользователь не найден");
        }
    }
}


