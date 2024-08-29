package com.example.wikibackend.repository;

import com.example.wikibackend.model.UserSpaceAccess;
import com.example.wikibackend.model.Space;
import com.example.wikibackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSpaceAccessRepository extends JpaRepository<UserSpaceAccess, Long> {
    List<UserSpaceAccess> findByUser(User user);

    List<UserSpaceAccess> findBySpace(Space space);
}

