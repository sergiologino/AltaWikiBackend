package com.example.wikibackend.service;

import com.example.wikibackend.config.SwitchSchema;
import com.example.wikibackend.dto.RoleDTO;
import com.example.wikibackend.model.Role;
import com.example.wikibackend.repository.RoleRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    @SwitchSchema
    public List<Role> getAllRoles(UUID organizationId) {
        return roleRepository.findAll();
    }

    @Transactional
    @SwitchSchema
    public Role addRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setRole_name(roleDTO.getName());
        return roleRepository.save(role);
    }

    @Transactional
    @SwitchSchema
    public Role updateRole(UUID id, RoleDTO roleDTO) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            role.setRole_name(roleDTO.getName());
            return roleRepository.save(role);
        } else {
            throw new IllegalArgumentException("Role not found");
        }
    }
}

