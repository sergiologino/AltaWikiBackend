package com.example.wikibackend.dto;

import com.example.wikibackend.model.DocumentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentDTO {
    private String title;
    private DocumentStatus status;
    private String author;
    private Long spaceId;
    private Long parentId;

    // Конструкторы, геттеры и сеттеры

    public DocumentDTO() {}

    public DocumentDTO(String title, DocumentStatus status, String author, Long spaceId, Long parentId) {
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

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}

