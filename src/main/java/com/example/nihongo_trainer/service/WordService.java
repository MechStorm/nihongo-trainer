package com.example.nihongo_trainer.service;

import com.example.nihongo_trainer.dto.WordDto;
import com.example.nihongo_trainer.entity.Category;
import com.example.nihongo_trainer.entity.Word;
import com.example.nihongo_trainer.repository.CategoryRepository;
import com.example.nihongo_trainer.repository.WordRepository;
import com.example.nihongo_trainer.utility.WordMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
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

        MultipartFile imageFile = wordDto.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                word.setImagePath("/uploads/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }

        return wordRepository.save(word);
    }

    public Word getWordById(Long id) {
        return wordRepository.findById(id).orElseThrow(() -> new RuntimeException("Word not found"));
    }

    @Transactional
    public Word updateWord(WordDto wordDto, MultipartFile image, boolean deleteImage) {
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

        if (deleteImage && word.getImagePath() != null) {
            try {
                Path imagePath = Paths.get("uploads", word.getImagePath().replace("/uploads/", ""));
                Files.deleteIfExists(imagePath);
                word.setImagePath(null);
            } catch (IOException e) {
                throw new RuntimeException("failed to delete image", e);
            }
        }

        if (image != null && !image.isEmpty()) {
            try {
                if (word.getImagePath() != null) {
                    Path oldImagePath = Paths.get("uploads" + word.getImagePath().replace("/uploads/", ""));
                    Files.deleteIfExists(oldImagePath);
                }

                String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path uploadPath = Paths.get("uploads");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                word.setImagePath("/uploads/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }

        return wordRepository.save(word);
    }

    @Transactional
    public void deleteWord(Long id) {
        Word word = wordRepository.findById(id).orElseThrow(() -> new RuntimeException("Word not found"));
        if (word.getImagePath() != null) {
            try {
                Path imagePath = Paths.get("uploads" + word.getImagePath().replace("/uploads/", ""));
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete image", e);
            }
        }
        wordRepository.deleteById(id);
    }

    public Page<WordDto> searchWords(String query,
                                     String sortField,
                                     Sort.Direction direction,
                                     Pageable pageable) {
        System.out.println("Search query: " + query);
        if (query == null || query.trim().isEmpty()) {
//            return getWordsByCategory(null, sortField, direction, pageable);
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortField));
        Page<Word> wordPage = wordRepository.searchWords(query, sortedPageable);
        return wordPage.map(WordMapper::toDto);
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

    public List<WordDto> autocomplete(String term) {
        if (term == null || term.trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<Word> words = wordRepository.findWordByTerm(term);

        return words.stream()
                .map(WordMapper::toDto)
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<WordDto> getAllWordsForFlashCards() {
        return wordRepository.findAll()
                .stream()
                .map(WordMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateWordStatus(Long id, boolean isLearned, boolean needsReview) {
        Word word = wordRepository.findById(id).orElseThrow(() -> new RuntimeException("Word not found"));
        word.setLearned(isLearned);
        word.setNeedsReview(needsReview);
        wordRepository.save(word);
    }
}
