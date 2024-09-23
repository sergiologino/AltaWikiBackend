package com.example.wikibackend.service;

import com.example.wikibackend.model.DocumentStatus;
import com.example.wikibackend.model.mongodb.WikiContent;
import com.example.wikibackend.repository.mongodb.WikiContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WikiContentService {

    private final WikiContentRepository wikiContentRepository;

    @Autowired
    public WikiContentService(WikiContentRepository wikiContentRepository) {
        this.wikiContentRepository = wikiContentRepository;
    }

    // Сохранение нового черновика или обновление существующего для конкретного пользователя
    public WikiContent saveDraft(WikiContent wikiContent) {
        // Найти существующий черновик для данного пользователя и документа
        Optional<WikiContent> existingDraft = wikiContentRepository.findByDocumentIdAndStatusAndUserId(
                wikiContent.getDocumentId(), DocumentStatus.DRAFT, wikiContent.getUserId());

        if (existingDraft.isPresent()) {
            WikiContent draft = existingDraft.get();
            draft.setContent(wikiContent.getContent());
            draft.setCreatedAt(LocalDateTime.now());
            return wikiContentRepository.save(draft);
        } else {
            wikiContent.setStatus(DocumentStatus.DRAFT);
            wikiContent.setCreatedAt(LocalDateTime.now());
            return wikiContentRepository.save(wikiContent);
        }
    }

    // Публикация документа, обновление статусов
    public WikiContent publishDocument(UUID documentId, String version, UUID author) {
        // Установить все активные версии как устаревшие
        Optional<WikiContent> activeContent = wikiContentRepository.findByDocumentIdAndStatusAndUserIdIsNull(documentId, DocumentStatus.ACTIVE);
        if (activeContent.isPresent()) {
            WikiContent outdatedContent = activeContent.get();
            outdatedContent.setStatus(DocumentStatus.OUTDATED);
            wikiContentRepository.save(outdatedContent);
        }

        // Найти текущий черновик и опубликовать его
        Optional<WikiContent> draftContent = wikiContentRepository.findByDocumentIdAndStatusAndUserId(documentId, DocumentStatus.DRAFT, null);
        if (draftContent.isPresent()) {
            WikiContent content = draftContent.get();
            content.setStatus(DocumentStatus.ACTIVE);
            content.setVersion(version);
            content.setCreatedAt(LocalDateTime.now());
            content.setAuthor(author);
            content.setUserId(null); // Убираем привязку к пользователю, так как это теперь активная версия
            return wikiContentRepository.save(content);
        } else {
            throw new IllegalStateException("No draft found for document ID: " + documentId);
        }
    }

    // Получение всех версий документа по его ID
    public List<WikiContent> getAllVersionsByDocumentId(UUID documentId) {
        return wikiContentRepository.findByDocumentId(documentId);
    }

    // Получение текущей активной версии документа по его ID
    public Optional<WikiContent> getCurrentVersionByDocumentId(UUID documentId) {
        return wikiContentRepository.findByDocumentIdAndStatusAndUserIdIsNull(documentId, DocumentStatus.ACTIVE);
    }

    // Получение черновика документа по его ID для конкретного пользователя, если он существует
    public Optional<WikiContent> getDraftByDocumentIdAndUserId(UUID documentId, UUID userId) {
        return wikiContentRepository.findByDocumentIdAndStatusAndUserId(documentId, DocumentStatus.DRAFT, userId);
    }
}
