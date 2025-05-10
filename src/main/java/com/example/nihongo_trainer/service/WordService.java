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

    public Word addWord(Word word) {
        return wordRepository.save(word);
    }

    public Word getWordById(Long id) {
        return wordRepository.findById(id).orElseThrow(() -> new RuntimeException("Word not found"));
    }

    public Optional<Word> updateWord(Long id, Word updatedWord) {
        return wordRepository.findById(id).map(word -> {
            word.setJapanese(updatedWord.getJapanese());
            word.setTranslation(updatedWord.getTranslation());
            word.setExample(updatedWord.getExample());
            word.setCreatedAt(updatedWord.getCreatedAt());

            return wordRepository.save(word);
        });
    }

    public void deleteWord(Long id) {
        wordRepository.deleteById(id);
    }
}
