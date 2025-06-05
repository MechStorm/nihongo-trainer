package com.example.nihongo_trainer.controller;

import com.example.nihongo_trainer.dto.WordDto;
import com.example.nihongo_trainer.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/flashcards")
public class FlashcardController {
    private final WordService wordService;

    @Autowired
    public FlashcardController(WordService wordService) {
        this.wordService = wordService;
    }

    @GetMapping
    public String showFlashcards(Model model) {
        List<WordDto> words = wordService.getAllWordsForFlashCards();
        model.addAttribute("words", words);
        return "flashcard";
    }

    public void updateWordStatus(@PathVariable Long id,
                                 @RequestParam("isLearned") boolean isLearned,
                                 @RequestParam("needsReview") boolean needsReview) {
        wordService.updateWordStatus(id, isLearned, needsReview);
    }
}
