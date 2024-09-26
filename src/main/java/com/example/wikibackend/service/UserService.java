package com.example.wikibackend.service;

import com.example.wikibackend.config.SwitchSchema;
import com.example.wikibackend.config.TenantContext;
import com.example.wikibackend.dto.UserDTO;
import com.example.wikibackend.model.User;
import com.example.wikibackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    JdbcTemplate jdbcTemplate;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;


        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @SwitchSchema
    public User addUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setEnabled(true);
        user.setDeleted(false);  // Устанавливаем значение false по умолчанию
        System.out.println("Current tenant in service: "+ TenantContext.getCurrentTenant());

        String sql = "INSERT INTO admin.user_organization (user_id, organization_id, username) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getId(), userDTO.getOrganizationId(), user.getUsername());

        return userRepository.save(user); // Сохраняем пользователя и получаем его ID
    }

    @SwitchSchema
    public boolean authenticateUser(String username, String password) {

        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getPassword());
    }

    @SwitchSchema
    public List<User> getAllUsers() {
        return userRepository.findAllByDeletedFalse();
    }

    @SwitchSchema
    public boolean deleteUser(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setDeleted(true); // Установка признака "deleted"
            userRepository.save(user);
            return true;
        }
        return false;
    }
    @SwitchSchema
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById( id);
    }

}
