package com.example.nihongo_trainer.dto;

import java.time.LocalDateTime;

public class WordDto {
    private Long id;
    private String japanese;
    private String translation;
    private String example;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WordDto(Long id, String japanese, String translation, String example, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.japanese = japanese;
        this.translation = translation;
        this.example = example;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJapanese() {
        return japanese;
    }

    public void setJapanese(String japanese) {
        this.japanese = japanese;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
