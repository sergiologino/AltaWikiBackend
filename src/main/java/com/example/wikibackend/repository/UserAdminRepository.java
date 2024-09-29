package com.example.wikibackend.repository;

import com.example.wikibackend.model.User;
import com.example.wikibackend.model.UserAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAdminRepository extends JpaRepository<UserAdmin, UUID> {
        Optional<UserAdmin> findByUsername(String username);

        Optional<UserAdmin> findById(UUID Id);
}
