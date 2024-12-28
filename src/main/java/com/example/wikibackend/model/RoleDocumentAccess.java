package com.example.wikibackend.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "role_document_access")
public class RoleDocumentAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_type", nullable = false)
    private AccessType accessType;

    public RoleDocumentAccess(UUID id, Role role, Document document, AccessType accessType) {
        this.id = id;
        this.role = role;
        this.document = document;
        this.accessType = accessType;
    }

    public RoleDocumentAccess() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

    // Геттеры и сеттеры
}

