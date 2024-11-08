package com.example.wikibackend.repository;

import com.example.wikibackend.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    @Query(value = "SELECT * FROM admin.organizations WHERE id = :id", nativeQuery = true)
    Optional<Organization> findById(@Param("id") UUID id);
}
