package com.example.wikibackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseDTO {
    private String organization;

    public BaseDTO() {}

    public BaseDTO(String organization) {
        this.organization = organization;
    }
}
