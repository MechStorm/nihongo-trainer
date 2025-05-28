package com.example.nihongo_trainer.service;

import com.example.nihongo_trainer.dto.CategoryDto;
import com.example.nihongo_trainer.entity.Category;
import com.example.nihongo_trainer.entity.Word;
import com.example.nihongo_trainer.repository.CategoryRepository;
import com.example.nihongo_trainer.repository.WordRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final WordRepository wordRepository;

    public CategoryService(CategoryRepository categoryRepository, WordRepository wordRepository) {
        this.categoryRepository = categoryRepository;
        this.wordRepository = wordRepository;
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAllWithWordCount();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Transactional
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category is not found: " + id));

        category.setName(categoryDto.getName());

        categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));

        List<Word> words = category.getWords();
        for (Word word : words) {
            word.setCategory(null);
        }
        wordRepository.saveAll(words);

        categoryRepository.deleteById(id);
    }
}
