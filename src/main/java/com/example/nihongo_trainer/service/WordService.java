package com.example.nihongo_trainer.service;

import com.example.nihongo_trainer.dto.WordDto;
import com.example.nihongo_trainer.entity.Category;
import com.example.nihongo_trainer.entity.Word;
import com.example.nihongo_trainer.repository.CategoryRepository;
import com.example.nihongo_trainer.repository.WordRepository;
import com.example.nihongo_trainer.utility.WordMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class WordService {
    private final WordRepository wordRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public WordService(WordRepository wordRepository, CategoryRepository categoryRepository) {
        this.wordRepository = wordRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Word> getAllWords() {
        return wordRepository.findAll();
    }

    public Word addWord(WordDto wordDto) {
        Word word = new Word();
        word.setJapanese(wordDto.getJapanese());
        word.setTranslation(wordDto.getTranslation());
        word.setExample(wordDto.getExample());

        if (wordDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(wordDto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
            word.setCategory(category);
        } else {
            word.setCategory(null);
        }

        return wordRepository.save(word);
    }

    public Word getWordById(Long id) {
        return wordRepository.findById(id).orElseThrow(() -> new RuntimeException("Word not found"));
    }

    @Transactional
    public Word updateWord(WordDto wordDto) {
        Word word = wordRepository.findById(wordDto.getId()).orElseThrow(() -> new RuntimeException("Word not found"));
        word.setJapanese(wordDto.getJapanese());
        word.setTranslation(wordDto.getTranslation());
        word.setExample(wordDto.getExample());

        if (wordDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(wordDto.getCategoryId()).orElseThrow();
            word.setCategory(category);
        } else {
            word.setCategory(null);
        }

        return wordRepository.save(word);
    }

    @Transactional
    public void deleteWord(Long id) {
        wordRepository.deleteById(id);
    }

    public List<WordDto> searchWords(String query) {
        return wordRepository.searchWords(query).stream()
                .map(WordMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<Word> getRandomWord() {
        List<Word> allWords = wordRepository.findAll();

        if (allWords.isEmpty()) return Optional.empty();

        Random random = new Random();
        return Optional.of(allWords.get(random.nextInt(allWords.size())));
    }

    public List<WordDto> getAllWordsSorted(String field, Sort.Direction direction) {
        return wordRepository.findAll(Sort.by(direction, field))
                .stream()
                .map(WordMapper::toDto)
                .collect(Collectors.toList());
    }

    public Page<WordDto> getWordsByCategory(String categoryId,
                                            String sortField,
                                            Sort.Direction direction,
                                            Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortField));
        Page<Word> wordPage;

        if (categoryId == null || categoryId.isEmpty()) {
            wordPage = wordRepository.findAll(sortedPageable);
        } else if (categoryId.equals("none")) {
            wordPage = wordRepository.findByCategoryIsNull(sortedPageable);
        } else {
            Long id = Long.parseLong(categoryId);
            wordPage = wordRepository.findByCategoryId(id, sortedPageable);
        }

        return wordPage.map(WordMapper::toDto);
    }
}
