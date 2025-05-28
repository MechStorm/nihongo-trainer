package com.example.nihongo_trainer.dto;

public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private Long wordCount;

    public CategoryDto() {}

    public CategoryDto(Long id, String name, String description, Long wordCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.wordCount = wordCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getWordCount() {
        return wordCount;
    }

    public void setWordCount(Long wordCount) {
        this.wordCount = wordCount;
    }
}
