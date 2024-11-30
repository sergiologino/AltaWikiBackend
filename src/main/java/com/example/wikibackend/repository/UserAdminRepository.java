package com.example.wikibackend.repository;

import com.example.wikibackend.model.UserAdmin;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserAdminRepository extends JpaRepository<UserAdmin, UUID> {
        @Modifying
        @Transactional
        @Query(value = "INSERT INTO admin.user_organization (user_id, organization_id, username, id) " +
                "VALUES (gen_random_uuid(), :organizationId, :username, :id)", nativeQuery = true)
        int addUserToOrganization(@Param("organizationId") UUID organizationId, @Param("username") String username, @Param("id") UUID id);
        }