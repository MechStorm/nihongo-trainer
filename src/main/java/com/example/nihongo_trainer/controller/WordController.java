package com.example.nihongo_trainer.controller;

import com.example.nihongo_trainer.entity.Word;
import com.example.nihongo_trainer.service.WordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/words")
public class WordController {
    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping
    public List<Word> getAllWords(@RequestParam(required = false) String level) {
        return (level == null) ? wordService.getAllWords() : wordService.getWordsByLevel(level);
    }

    @PostMapping
    public Word addWord(@RequestBody Word word) {
        return wordService.addWord(word);
    }

    @GetMapping("/{id}")
    public Word getWordById(@PathVariable Long id) {
        return wordService.getWordById(id)
                .orElseThrow(() -> new RuntimeException("Word not found!"));
    }
}
