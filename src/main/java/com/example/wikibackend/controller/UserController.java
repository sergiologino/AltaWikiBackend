package com.example.wikibackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody String user) {
        return ResponseEntity.ok("User registered successfully");
    }

    @Operation(summary = "Authenticate user")
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody String credentials) {
        return ResponseEntity.ok("User logged in successfully");
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok("User deleted successfully");
    }
}

