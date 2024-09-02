package com.example.wikibackend.controller;

import com.example.wikibackend.dto.UserDTO;
import com.example.wikibackend.model.User;
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

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Создание нового пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class),
                            examples = @ExampleObject(value = "{\"username\": \"john_doe\", \"password\": \"password123\", \"email\": \"john@example.com\"}"))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
                            content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
            })
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO) {
        User user = userService.addUser(userDTO);
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
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.ok("Пользователь успешно помечен как удаленный");
        } else {
            return ResponseEntity.status(404).body("Пользователь не найден");
        }
    }
}


