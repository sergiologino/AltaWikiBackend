package com.example.wikibackend.dto;

import com.example.wikibackend.model.DocumentStatus;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DocumentDTO extends BaseDTO {
    private String title;
    private DocumentStatus status;
    private String author;
    private UUID spaceId;
    private UUID parentId;

    public DocumentDTO() {}

    public DocumentDTO(String title, DocumentStatus status, String author, UUID spaceId, UUID parentId, String organization) {
        super(organization);
        this.title = title;
        this.status = status;
        this.author = author;
        this.spaceId = spaceId;
        this.parentId = parentId;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public UUID getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(UUID spaceId) {
        this.spaceId = spaceId;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
}
