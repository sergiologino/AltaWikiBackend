package com.example.wikibackend.repository;

import com.example.wikibackend.config.SwitchSchema;
import com.example.wikibackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    // Метод для получения всех активных пользователей (которые не удалены)
    List<User> findAllByDeletedFalse();

    Optional<User> findById(UUID Id);
}

