package com.example.nihongo_trainer.service;

import com.example.nihongo_trainer.dto.WordDto;
import com.example.nihongo_trainer.entity.Word;
import com.example.nihongo_trainer.repository.WordRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class WordService {
    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<Word> getAllWords() {
        return wordRepository.findAll();
    }

    public Word addWord(Word word) {
        return wordRepository.save(word);
    }

    public Word getWordById(Long id) {
        return wordRepository.findById(id).orElseThrow(() -> new RuntimeException("Word not found"));
    }

    @Transactional
    public Optional<Word> updateWord(Long id, Word updatedWord) {
        return wordRepository.findById(id).map(word -> {
            word.setJapanese(updatedWord.getJapanese());
            word.setTranslation(updatedWord.getTranslation());
            word.setExample(updatedWord.getExample());
            word.setCreatedAt(updatedWord.getCreatedAt());

            return wordRepository.save(word);
        });
    }

    @Transactional
    public void deleteWord(Long id) {
        wordRepository.deleteById(id);
    }

    public List<Word> searchWords(String query) {
        return wordRepository.findByJapaneseContainingIgnoreCaseOrTranslationContainingIgnoreCase(query, query);
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
                .map(word -> new WordDto(
                        word.getId(),
                        word.getJapanese(),
                        word.getTranslation(),
                        word.getExample(),
                        word.getCreatedAt()
                                .atZone(ZoneId.of("UTC"))
                                .withZoneSameInstant(ZoneId.of("Europe/Moscow"))
                                .toLocalDateTime(),
                        word.getUpdatedAt() != null
                        ? word.getUpdatedAt().atZone(ZoneId.of("UTC"))
                                .withZoneSameInstant(ZoneId.of("Europe/Moscow"))
                                .toLocalDateTime()
                                : null

                ))
                .collect(Collectors.toList());
    }
}
