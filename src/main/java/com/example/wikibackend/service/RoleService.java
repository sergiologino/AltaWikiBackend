package com.example.wikibackend.service;

import com.example.wikibackend.model.Role;
import com.example.wikibackend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Создание новой роли
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    // Получение всех ролей
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Поиск роли по ID
    public Optional<Role> findRoleById(Long id) {
        return roleRepository.findById(id);
    }

    // Удаление роли
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}

