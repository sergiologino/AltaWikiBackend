package com.example.wikibackend.repository;

import com.example.wikibackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    // Метод для получения всех активных пользователей (которые не удалены)
    List<User> findAllByDeletedFalse();
}

