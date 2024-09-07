package com.example.wikibackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO extends BaseDTO {
    private String username;
    private String email;

    public UserDTO() {}

    public UserDTO(String username, String email, String organization) {
        super(organization);
        this.username = username;
        this.email = email;
    }
}
