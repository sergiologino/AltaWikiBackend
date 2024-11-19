package com.example.wikibackend.controller;

import com.example.wikibackend.config.TenantContext;
import com.example.wikibackend.dto.UserDTO;
import com.example.wikibackend.mapper.OrganizationMapper;
import com.example.wikibackend.model.Organization;
import com.example.wikibackend.model.User;
import com.example.wikibackend.model.UserAdmin;
import com.example.wikibackend.repository.OrganizationRepository;
import com.example.wikibackend.service.OrganizationService;
import com.example.wikibackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final OrganizationService organizationService;
    JdbcTemplate jdbcTemplate;

    @Autowired
    public UserController(UserService userService, OrganizationService organizationService) {
        this.userService = userService;
        this.organizationService = organizationService;

    }

    @Operation(summary = "Создание нового пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class),
                            examples = @ExampleObject(value = "{\"organizationId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"username\": \"Vasya (english only) \", \"password\": \"123\", \"email\": \"sample@altacod.ru\"}"))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
                            content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
            })
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO) {
        // Устанавливаем organizationId в TenantContext

        Long aliasOrg = organizationService.getAlias(userDTO.getOrganizationId());
        if (aliasOrg == null) {
            System.out.println("Не удалось получить организацию, проверьте авторизацию");
            return ResponseEntity.badRequest().build();
        }

        TenantContext.setCurrentTenant(aliasOrg);
        System.out.println("Current tenant in controller: "+TenantContext.getCurrentTenant());
        //UserAdmin userAdmin=userService.addUserAdmin(userDTO);
        TenantContext.setCurrentTenant(aliasOrg);
        User user = userService.addUser(userDTO);

        // Очищаем контекст после выполнения операции
        TenantContext.clear();
        return ResponseEntity.status(201).body(user);
    }

    @Operation(summary = "Авторизация пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class),
                            examples = @ExampleObject(value = "{\"organizationId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"username\": \"Сергей\", \"password\": \"123\"}"))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Авторизация успешна"),
                    @ApiResponse(responseCode = "401", description = "Некорректные учетные данные")
            })
    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody UserDTO userDTO) {
//        String sql = "SELECT admin.organization_id FROM admin.user_organization WHERE username = ?";
//        UUID organizationId = jdbcTemplate.queryForObject(sql, new Object[]{userDTO.getUsername()}, UUID.class);
        UUID currentOrganizationId = userDTO.getOrganizationId();
        Long aliasOrg = organizationService.getAlias(userDTO.getOrganizationId());
        if (aliasOrg == null) {
            System.out.println("Не удалось получить организацию в методе loginUser, проверьте авторизацию");
            return ResponseEntity.badRequest().build();
        }
        TenantContext.setCurrentTenant(aliasOrg);
        // Бизнес-логика авторизации пользователя
        boolean isAuthenticated = userService.authenticateUser(userDTO.getUsername(), userDTO.getPassword());
        TenantContext.clear();
        if (isAuthenticated) {
            userDTO.setOrganization(currentOrganizationId);
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(401).body(userDTO);
        }
    }

    @Operation(
            summary = "Получение списка всех пользователей по ID организации",
            parameters = {
                    @Parameter(
                            name = "organizationId",
                            description = "UUID организации",
                            required = true,
                            schema = @Schema(type = "string", format = "uuid")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список всех пользователей",
                            content = @Content(schema = @Schema(implementation = User.class))
                    )
            }
    )
    @GetMapping("/{organizationId}")
    public ResponseEntity<List<User>> getAllUsers(@PathVariable UUID organizationId) {
        Long aliasOrg = organizationService.getAlias(organizationId);
        if (aliasOrg == null) {
            return ResponseEntity.badRequest().build();
        }
        TenantContext.setCurrentTenant(aliasOrg);
        System.out.println("Current tenant in controller, get all users: "+TenantContext.getCurrentTenant());
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Удаление пользователя (установка признака 'deleted')",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь успешно помечен как удаленный"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID organizationId, UUID id) {
        Long aliasOrg = organizationService.getAlias(organizationId);
        if (aliasOrg == null) {
            return ResponseEntity.badRequest().body("Не удалось получить организацию, проверьте авторизацию");
        }
        TenantContext.setCurrentTenant(aliasOrg);
        System.out.println("Current tenant in controller: "+TenantContext.getCurrentTenant());
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.ok("Пользователь успешно помечен как удаленный");
        } else {
            return ResponseEntity.status(404).body("Пользователь не найден");
        }
    }
}


