package com.example.nihongo_trainer.service;

import com.example.nihongo_trainer.entity.Word;
import com.example.nihongo_trainer.repository.WordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WordService {
    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<Word> getAllWords() {
        return wordRepository.findAll();
    }

    public List<Word> getWordsByLevel(String level) {
        return wordRepository.findByLevel(level);
    }

    public Word addWord(Word word) {
        return wordRepository.save(word);
    }

    public Optional<Word> getWordById(Long id) {
        return wordRepository.findById(id);
    }
}
