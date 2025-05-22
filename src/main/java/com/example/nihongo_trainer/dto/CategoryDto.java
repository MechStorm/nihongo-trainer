package com.example.nihongo_trainer.dto;

public class CategoryDto {
    private Long id;
    private String name;
    private Long wordCount;

    public CategoryDto() {}

    public CategoryDto(Long id, String name, Long wordCount) {
        this.id = id;
        this.name = name;
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

    public Long getWordCount() {
        return wordCount;
    }

    public void setWordCount(Long wordCount) {
        this.wordCount = wordCount;
    }
}
