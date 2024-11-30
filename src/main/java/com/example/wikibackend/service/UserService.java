package com.example.wikibackend.service;

import com.example.wikibackend.config.SchemaContext;
import com.example.wikibackend.config.SchemaInterceptor;
import com.example.wikibackend.config.SwitchSchema;
import com.example.wikibackend.dto.UserDTO;
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


    @Autowired
    private final SchemaContext schemaContext;
    private final OrganizationService organizationService;

    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    public UserService(UserRepository userRepository, UserAdminRepository userAdminRepository, JdbcTemplate jdbcTemplate, SchemaContext schemaContext, OrganizationService organizationService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userAdminRepository = userAdminRepository;
        this.schemaContext = schemaContext;
        this.organizationService = organizationService;


        //     this.jdbcTemplate = jdbcTemplate;


        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Optional<UserAdmin> addUserAdmin(UserDTO userDTO, UUID organizationId){
        UserAdmin userAdmin = new UserAdmin();
        userAdmin.setUsername(userDTO.getUsername());
        userAdmin.setId(UUID.fromString(UUID.randomUUID().toString()));
        userAdmin.setOrganizationId(userDTO.getOrganizationId());
        System.out.println("organizationId : " + organizationId);
        int result= userAdminRepository.addUserToOrganization(organizationId, userAdmin.getUsername(),organizationId);
        return result > 0 ? Optional.of(userAdmin) : Optional.empty();

    }

    @Transactional
    @SwitchSchema
    public User addUser(UserDTO userDTO, UUID organizationId) {
        addUserAdmin(userDTO, userDTO.getOrganizationId());
        checkSchema("проверяем схему в UserService метод addUser в самом начале");
        System.out.println("alias : " + organizationService.getAlias(organizationId));
        schemaContext.setSchema("alt_"+organizationService.getAlias(organizationId));
        setSchema("alt_"+organizationService.getAlias(organizationId));
        System.out.println("назначили схему через schemaContext.getCurrentSchema, ожидаем alt_x " + schemaContext.getCurrentSchema());
        checkSchema("проверяем схему в UserService метод addUser");


        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setEnabled(true);
        user.setDeleted(false);  // Устанавливаем значение false по умолчанию

        System.out.println("organizationId: "+organizationId);
        System.out.println("Записываем юзера в свою схему "+user);
        checkSchema("проверяем схему в UserService метод addUser перед записью через userRepository.save");
        entityManager.flush();
        entityManager.clear();
        return userRepository.save(user); // Сохраняем пользователя и получаем его ID
    }

    @Transactional
    @SwitchSchema
    public boolean authenticateUser(String username, String password, UUID organizationId) throws SQLException {
        checkSchema("проверяем схему в UserService метод authenticateUser перед поиском через userRepository.findByUsername");
        //schemaContext.setSchema("alt_"+organizationService.getAlias(organizationId));
        setSchema("alt_"+organizationService.getAlias(organizationId));
        System.out.println("назначили схему через schemaContext.getCurrentSchema, ожидаем alt_x " + schemaContext.getCurrentSchema());
        //checkSchema("проверяем схему в UserService метод authenticateUser");

        Optional<User> userOptional = userRepository.findByUsername(username);
        log.info("founded userName {}",userOptional);
        return userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getPassword());
    }

    @Transactional
    @SwitchSchema
    public List<User> getAllUsers(UUID organizationId) {
        checkSchema("проверяем схему в UserService перед поиском в getAllUsers");
        //schemaContext.setSchema("alt_"+organizationService.getAlias(organizationId));
        setSchema("alt_"+organizationService.getAlias(organizationId));
        System.out.println("назначили схему через schemaContext.getCurrentSchema, ожидаем alt_x " + schemaContext.getCurrentSchema());
        checkSchema("проверяем схему в UserService метод getAllUsers");
        return userRepository.findAllByDeletedFalse(); }

    @Transactional
    @SwitchSchema
    public boolean deleteUser(UUID id, UUID organizationId) {
        checkSchema("проверяем схему в UserService метод authenticateUser перед deleteUser");
        //schemaContext.setSchema("alt_"+organizationService.getAlias(organizationId));
        setSchema("alt_"+organizationService.getAlias(organizationId));
        System.out.println("назначили схему через schemaContext.getCurrentSchema, ожидаем alt_x " + schemaContext.getCurrentSchema());
        checkSchema("проверяем схему в UserService метод deleteUser");

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
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById( id);
    }

    public void checkSchema(String message) {
        String currentSchema = (String) entityManager.createNativeQuery("SHOW search_path").getSingleResult();
        System.out.println("чекер: " + message+" , "+currentSchema);

    }
    public void setSchema(String schema) {
        schemaContext.setSchema(schema);
        entityManager.createNativeQuery("SET search_path TO " + schema).executeUpdate();
        System.out.println(" in method setSchema: "+schemaContext.getCurrentSchema());
    }

}
