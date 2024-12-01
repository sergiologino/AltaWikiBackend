package com.example.wikibackend.service;

import com.example.wikibackend.config.SchemaContext;
import com.example.wikibackend.config.SchemaInterceptor;
import com.example.wikibackend.config.SwitchSchema;
import com.example.wikibackend.dto.UserDTO;
import com.example.wikibackend.mapper.UserMapper;
import com.example.wikibackend.model.Organization;
import com.example.wikibackend.model.User;
import com.example.wikibackend.model.UserAdmin;
import com.example.wikibackend.repository.UserAdminRepository;
import com.example.wikibackend.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {


    private final UserRepository userRepository;
    private final UserAdminRepository userAdminRepository;
    private final SchemaService schemaService;
    private final UserMapper userMapper;


    @Autowired
    private final SchemaContext schemaContext;
    private final OrganizationService organizationService;

    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    public UserService(UserRepository userRepository, UserAdminRepository userAdminRepository, JdbcTemplate jdbcTemplate, SchemaService schemaService, UserMapper userMapper, SchemaContext schemaContext, OrganizationService organizationService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userAdminRepository = userAdminRepository;
        this.schemaService = schemaService;
        this.userMapper = userMapper;
        this.schemaContext = schemaContext;
        this.organizationService = organizationService;


        //     this.jdbcTemplate = jdbcTemplate;


        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Optional<UserAdmin> addUserAdmin(UserDTO userDTO, UUID organizationId) {
        UserAdmin userAdmin = new UserAdmin();
        userAdmin.setUsername(userDTO.getUsername());
        userAdmin.setId(UUID.fromString(UUID.randomUUID().toString()));
        userAdmin.setOrganizationId(userDTO.getOrganizationId());
        System.out.println("organizationId : " + organizationId);
        int result = userAdminRepository.addUserToOrganization(organizationId, userAdmin.getUsername(), organizationId);
        return result > 0 ? Optional.of(userAdmin) : Optional.empty();

    }

    @Transactional
    @SwitchSchema
    public User addUser(UserDTO userDTO, UUID organizationId) {
        addUserAdmin(userDTO, userDTO.getOrganizationId());
        schemaService.setSchema(organizationId);

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setEnabled(true);
        user.setDeleted(false);  // Устанавливаем значение false по умолчанию
        return userRepository.save(user); // Сохраняем пользователя и получаем его ID
    }

    @Transactional
    @SwitchSchema
    public boolean authenticateUser(String username, String password, UUID organizationId) throws SQLException {
        schemaService.setSchema(organizationId);
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getPassword());
    }

    @Transactional
    @SwitchSchema
    public List<User> getAllUsers(UUID organizationId) {

        schemaService.setSchema(organizationId);
        return userRepository.findAllByDeletedFalse();
    }

    @Transactional
    @SwitchSchema
    public boolean deleteUser(UUID id, UUID organizationId) {

        schemaService.setSchema(organizationId);

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setDeleted(true); // Установка признака "deleted"
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Transactional
    @SwitchSchema
    public Optional<UserDTO> getUserById(UUID id, UUID organizationId) {
        schemaService.setSchema(organizationId);
        return Optional.ofNullable(userMapper.toDTO(userRepository.findById(id).orElse(null)));


    }
}


