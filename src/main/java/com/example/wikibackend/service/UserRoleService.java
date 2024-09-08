package com.example.wikibackend.service;

import com.example.wikibackend.model.Role;
import com.example.wikibackend.model.User;
import com.example.wikibackend.repository.RoleRepository;
import com.example.wikibackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserRoleService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public boolean assignRoleToUser(UUID userId, UUID roleId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Role> roleOptional = roleRepository.findById(roleId);

        if (userOptional.isPresent() && roleOptional.isPresent()) {
            User user = userOptional.get();
            Role role = roleOptional.get();
            user.getRoles().add(role);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    public boolean removeRoleFromUser(UUID userId, UUID roleId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Role> roleOptional = roleRepository.findById(roleId);

        if (userOptional.isPresent() && roleOptional.isPresent()) {
            User user = userOptional.get();
            Role role = roleOptional.get();
            user.getRoles().remove(role);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    public boolean changeUserRole(UUID userId, UUID roleId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Role> roleOptional = roleRepository.findById(roleId);

        if (userOptional.isPresent() && roleOptional.isPresent()) {
            User user = userOptional.get();
            user.getRoles().clear();
            user.getRoles().add(roleOptional.get());
            userRepository.save(user);
            return true;
        }

        return false;
    }
}
