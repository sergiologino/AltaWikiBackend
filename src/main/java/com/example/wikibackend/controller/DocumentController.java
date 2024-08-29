package com.example.wikibackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    // 1. Получение дерева всех документов
    @GetMapping("/tree")
    public ResponseEntity<String> getDocumentTree() {
        // Мок ответа
        return ResponseEntity.ok("Document tree retrieved successfully");
    }

    // 2. Получение дерева всех документов, подчиненных текущему
    @GetMapping("/tree/{id}")
    public ResponseEntity<String> getSubDocumentTree(@PathVariable Long id) {
        // Мок ответа
        return ResponseEntity.ok("Sub-document tree retrieved successfully");
    }

    // 3. Получение документа по id
    @GetMapping("/{id}")
    public ResponseEntity<String> getDocumentById(@PathVariable Long id) {
        // Мок ответа
        return ResponseEntity.ok("Document retrieved successfully");
    }

    // 4. Поиск документа по заголовку
    @GetMapping("/search")
    public ResponseEntity<String> searchDocumentByTitle(@RequestParam String title) {
        // Мок ответа
        return ResponseEntity.ok("Document found successfully");
    }

    // 5. Сохранение документа
    @PostMapping("/")
    public ResponseEntity<String> saveDocument(@RequestBody String document) {
        // Мок ответа
        return ResponseEntity.ok("Document saved successfully");
    }

    // 6. Изменение документа
    @PutMapping("/{id}")
    public ResponseEntity<String> updateDocument(@PathVariable Long id, @RequestBody String document) {
        // Мок ответа
        return ResponseEntity.ok("Document updated successfully");
    }

    // 7. Удаление документа
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id) {
        // Мок ответа
        return ResponseEntity.ok("Document deleted successfully");
    }
}
