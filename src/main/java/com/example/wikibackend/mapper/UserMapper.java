package com.example.wikibackend.mapper;

import com.example.wikibackend.dto.UserDTO;
import com.example.wikibackend.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Преобразование DTO в сущность Organization
    public User toEntity(UserDTO userDTO) {
        User newUser = new User();
        //organization.setID(organizationDTO.getId());
        newUser.setUsername(userDTO.getUsername());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(userDTO.getPassword());
        newUser.setDeleted(false);
        newUser.setEnabled(true);
        return newUser;
    }

    // Преобразование сущности Organization в DTO
    public UserDTO toDTO(User user) {
        UserDTO newUserDTO =new UserDTO();
        newUserDTO.setUsername(user.getUsername());
        newUserDTO.setEmail(user.getEmail());
        newUserDTO.setPassword(passwordEncoder.encode(user.getPassword()));
        return newUserDTO;
    }
}
