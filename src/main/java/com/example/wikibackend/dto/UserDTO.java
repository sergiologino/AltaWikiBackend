package com.example.wikibackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserDTO extends BaseDTO {
    private String username;
    private String password;
    private String email;


    public UserDTO() {}

    public UserDTO(String username, String email, UUID organizationId) {
        super(organizationId);
        this.username = username;
        this.email = email;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public String getOrganizationAlias() {
//        return getOrganizationAlias();
//    }
//
//    public void setOrganizationAlias(String organizationAlias) {
//
//        setOrganizationAlias(organizationAlias);
//    }
}
