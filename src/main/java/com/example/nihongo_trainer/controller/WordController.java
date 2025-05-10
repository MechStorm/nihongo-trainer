package com.example.nihongo_trainer.controller;

import com.example.nihongo_trainer.entity.Word;
import com.example.nihongo_trainer.service.WordService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/words")
public class WordController {
    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @PostMapping("/words-list")
    public String addWord(@ModelAttribute Word word) {
        wordService.addWord(word);
        return "redirect:/api/words/words-list";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Word> updateWord(@PathVariable Long id, @RequestBody Word updatedWord) {
        return wordService.updateWord(id, updatedWord)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Word getWordById(@PathVariable Long id) {
        return wordService.getWordById(id)
                .orElseThrow(() -> new RuntimeException("Word not found!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWord(@PathVariable Long id) {
        wordService.deleteWord(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/words-list")
    public String showWords(Model model) {
        model.addAttribute("words", wordService.getAllWords());
        model.addAttribute("newWord", new Word());
        return "word-list";
    }
}
