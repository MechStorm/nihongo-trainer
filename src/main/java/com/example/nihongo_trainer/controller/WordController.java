package com.example.nihongo_trainer.controller;

import com.example.nihongo_trainer.entity.Word;
import com.example.nihongo_trainer.service.WordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/words-list/edit")
    public String updateWord(@ModelAttribute Word word) {
        wordService.addWord(word);
        return "redirect:/api/words/words-list";
    }

    @PostMapping("/words-list/delete/{id}")
    public String deleteWord(@PathVariable Long id) {
        wordService.deleteWord(id);
        return "redirect:/api/words/words-list";
    }

    @GetMapping("/words-list")
    public String showWords(Model model) {
        model.addAttribute("words", wordService.getAllWordsSorted());
        model.addAttribute("newWord", new Word());
        return "word-list";
    }

    @GetMapping("/words-list/edit/{id}")
    public String editWord(@PathVariable Long id, Model model) {
        Word word = wordService.getWordById(id);
        model.addAttribute("word", word);
        return "edit-word";
    }
}
