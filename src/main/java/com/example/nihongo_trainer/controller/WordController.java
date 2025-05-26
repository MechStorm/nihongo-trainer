package com.example.nihongo_trainer.controller;

import com.example.nihongo_trainer.dto.CategoryDto;
import com.example.nihongo_trainer.dto.WordDto;
import com.example.nihongo_trainer.entity.Word;
import com.example.nihongo_trainer.service.CategoryService;
import com.example.nihongo_trainer.service.WordService;
import com.example.nihongo_trainer.utility.SortOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/words")
public class WordController {
    private final WordService wordService;
    private final CategoryService categoryService;

    @Autowired
    public WordController(WordService wordService, CategoryService categoryService) {
        this.wordService = wordService;
        this.categoryService = categoryService;
    }

    @PostMapping("/words-list")
    public String addWord(@ModelAttribute WordDto wordDto) {
        wordService.addWord(wordDto);
        return "redirect:/api/words/words-list";
    }

    @PostMapping("/words-list/edit")
    public String updateWord(@ModelAttribute WordDto wordDto) {
        wordService.updateWord(wordDto);
        return "redirect:/api/words/words-list";
    }

    @PostMapping("/words-list/delete/{id}")
    public String deleteWord(@PathVariable Long id) {
        wordService.deleteWord(id);
        return "redirect:/api/words/words-list";
    }

    @GetMapping("/words-list")
    public String showWords(@RequestParam(defaultValue = "CREATED_DESC") String sort,
                            @RequestParam(value = "categoryId", required = false) String categoryId,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "3") int size,
                            Model model) {
        if (page < 0) {
            page = 0;
        }

        SortOption sortOption = SortOption.from(sort);
        Pageable pageable = PageRequest.of(page, size);
        Page<WordDto> wordPage = wordService.getWordsByCategory(categoryId, sortOption.getField(), sortOption.getDirection(), pageable);
        //List<WordDto> words = wordService.getAllWordsSorted(sortOption.getField(), sortOption.getDirection());
        List<CategoryDto> categories = categoryService.getAllCategories();

        model.addAttribute("words", wordPage);
        model.addAttribute("newWord", new WordDto());
        model.addAttribute("categories", categories);
        model.addAttribute("currentSort", sortOption.name());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("currentPage", wordPage.getNumber());
        model.addAttribute("totalPages", wordPage.getTotalPages());
        model.addAttribute("totalItems", wordPage.getTotalElements());
        return "word-list";
    }

    @GetMapping("/words-list/edit/{id}")
    public String editWord(@PathVariable Long id, Model model) {
        Word word = wordService.getWordById(id);
        WordDto wordDto = new WordDto(
                word.getId(),
                word.getJapanese(),
                word.getTranslation(),
                word.getExample(),
                word.getCreatedAt(),
                word.getUpdatedAt(),
                word.getCategory() != null ? word.getCategory().getId() : null,
                word.getCategory() != null ? word.getCategory().getName() : null
        );

        model.addAttribute("word", wordDto);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit-word";
    }

    @GetMapping("/words-list/search")
    public String searchWords(@RequestParam(defaultValue = "CREATED_DESC") String sort,
                              @RequestParam("query") String query,
                              Model model) {
        SortOption sortOption = SortOption.from(sort);
        List<WordDto> words = wordService.searchWords(query);

        model.addAttribute("currentSort", sortOption.name());
        model.addAttribute("words", words);
        model.addAttribute("query", query);
        return "search-page";
    }

    /*@GetMapping("/words-list/filter")
    public String filterByCategory(@RequestParam(required = false) String categoryId,
                                   @RequestParam(defaultValue = "CREATED_DESC") String sort,
                                   Model model) {
        SortOption sortOption = SortOption.from(sort);
        List<WordDto> words = new ArrayList<>();
        if (categoryId == null || categoryId.isBlank()) {
            words = wordService.getAllWordsSorted(sortOption.getField(), sortOption.getDirection());
        } else if (categoryId.equals("none")) {
            words = wordService.getWordsWithoutCategory();
        }
        else {
            Long id = Long.parseLong(categoryId);
            words = wordService.getWordsByCategory(id);
        }

        model.addAttribute("words", words);
        model.addAttribute("newWord", new WordDto());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("categories", categoryService.getAllCategories());

        return "word-list";
    }*/
}
