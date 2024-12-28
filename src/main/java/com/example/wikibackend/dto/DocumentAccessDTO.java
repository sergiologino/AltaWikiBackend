package com.example.wikibackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentAccessDTO {
    private DocumentDTO document;
    private String accessType;

    public DocumentAccessDTO() {}

    public DocumentAccessDTO(DocumentDTO document, String accessType) {
        this.document = document;
        this.accessType = accessType;
    }
}

