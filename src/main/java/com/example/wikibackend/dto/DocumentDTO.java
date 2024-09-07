package com.example.wikibackend.dto;

import com.example.wikibackend.model.DocumentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentDTO extends BaseDTO {
    private String title;
    private DocumentStatus status;
    private String author;
    private Long spaceId;
    private Long parentId;

    public DocumentDTO() {}

    public DocumentDTO(String title, DocumentStatus status, String author, Long spaceId, Long parentId, String organization) {
        super(organization);
        this.title = title;
        this.status = status;
        this.author = author;
        this.spaceId = spaceId;
        this.parentId = parentId;
    }
}
