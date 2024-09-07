package com.example.wikibackend.service;

import com.example.wikibackend.dto.RoleDTO;
import com.example.wikibackend.model.Role;
import com.example.wikibackend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role addRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());
        return roleRepository.save(role);
    }

    public Role updateRole(UUID id, RoleDTO roleDTO) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            role.setName(roleDTO.getName());
            return roleRepository.save(role);
        } else {
            throw new IllegalArgumentException("Role not found");
        }
    }
}

