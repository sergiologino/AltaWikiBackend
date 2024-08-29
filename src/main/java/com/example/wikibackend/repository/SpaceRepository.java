package com.example.wikibackend.repository;

import com.example.wikibackend.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceRepository extends JpaRepository<Space, Long> {
}

