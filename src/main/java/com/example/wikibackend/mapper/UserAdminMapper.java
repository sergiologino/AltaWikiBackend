package com.example.wikibackend.mapper;

import com.example.wikibackend.dto.UserDTO;
import com.example.wikibackend.model.UserAdmin;
import org.springframework.stereotype.Component;

@Component
public class UserAdminMapper {



        // Преобразование DTO в сущность Organization
        public UserAdmin toEntity(UserDTO userDTO) {
            UserAdmin userAdmin = new UserAdmin();
            //organization.setID(organizationDTO.getId());
            userAdmin.setEmail(userDTO.getEmail());
            userAdmin.setUsername(userDTO.getUsername());
            userAdmin.setPassword(userDTO.getPassword());
            return userAdmin;
        }

        // Преобразование сущности Organization в DTO
        public UserDTO toDTO(UserAdmin userAdmin) {
            UserDTO newUserDTO =new UserDTO();
            newUserDTO.setUsername(userAdmin.getUsername());
            //newUserDTO(userAdmin.getId());
            return newUserDTO;
        }
}
