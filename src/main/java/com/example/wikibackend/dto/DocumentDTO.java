package com.example.wikibackend.dto;

import com.example.wikibackend.model.DocumentStatus;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;

import java.util.UUID;

@Getter
@Setter
public class DocumentDTO extends BaseDTO {

    private String title;
    private DocumentStatus status;
    private String content;
    private UUID authorId;
    private UUID spaceId;
    private UUID parentId;

    public DocumentDTO() {}

    public DocumentDTO(String title, DocumentStatus status, UUID author, UUID spaceId, UUID parentId, UUID organization, String content ) {
        super(organization);
        this.title = title;
        this.status = status;
        this.authorId = author;
        this.spaceId = spaceId;
        this.parentId = parentId;
        this.content = content;
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

    public UUID getAuthor() {
        return authorId;
    }

    public void setAuthor(UUID authorId) {
        this.authorId = authorId;
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

    public String getContent()
        {
            return content;
        }
    public void setContent(String content) {
            this.content = content;
        }
}
