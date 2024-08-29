package com.example.wikibackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    // 1. Создание роли
    @PostMapping("/")
    public ResponseEntity<String> createRole(@RequestBody String role) {
        // Мок ответа
        return ResponseEntity.ok("Role created successfully");
    }

    // 2. Получение списка ролей
    @GetMapping("/")
    public ResponseEntity<String> getRoles() {
        // Мок ответа
        return ResponseEntity.ok("Roles retrieved successfully");
    }

    // 3. Получение роли по id
    @GetMapping("/{id}")
    public ResponseEntity<String> getRoleById(@PathVariable Long id) {
        // Мок ответа
        return ResponseEntity.ok("Role retrieved successfully");
    }

    // 4. Удаление роли
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {
        // Мок ответа
        return ResponseEntity.ok("Role deleted successfully");
    }

    // 5. Присвоение роли пользователю
    @PostMapping("/assign")
    public ResponseEntity<String> assignRoleToUser(@RequestParam Long userId, @RequestParam Long roleId) {
        // Мок ответа
        return ResponseEntity.ok("Role assigned to user successfully");
    }

    // 6. Отключение роли от пользователя
    @PostMapping("/unassign")
    public ResponseEntity<String> unassignRoleFromUser(@RequestParam Long userId, @RequestParam Long roleId) {
        // Мок ответа
        return ResponseEntity.ok("Role unassigned from user successfully");
    }

    // 7. Изменение роли
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRole(@PathVariable Long id, @RequestBody String role) {
        // Мок ответа
        return ResponseEntity.ok("Role updated successfully");
    }
}
