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
import org.springframework.web.multipart.MultipartFile;

import java.time.ZoneId;
import java.util.Collections;
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
    public String updateWord(@ModelAttribute WordDto wordDto,
                             @RequestParam("image") MultipartFile image,
                             @RequestParam(value = "deleteImage", required = false) boolean deleteImage) {
        wordService.updateWord(wordDto, image, deleteImage);
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
        WordDto wordDto = new WordDto();
        wordDto.setId(word.getId());
        wordDto.setJapanese(word.getJapanese());
        wordDto.setTranslation(word.getTranslation());
        wordDto.setExample(word.getExample());

        wordDto.setCreatedAt(word.getCreatedAt()
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Europe/Moscow"))
                .toLocalDateTime());
        wordDto.setUpdatedAt(word.getUpdatedAt() != null
                ? word.getUpdatedAt().atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Europe/Moscow"))
                .toLocalDateTime()
                : null);

        wordDto.setCategoryId(word.getCategory() != null ? word.getCategory().getId() : null);
        wordDto.setCategoryName(word.getCategory() != null ? word.getCategory().getName() : null);

        wordDto.setImagePath(word.getImagePath());

        model.addAttribute("word", wordDto);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit-word";
    }

    @GetMapping("/words-list/search")
    public String searchWords(@RequestParam(defaultValue = "CREATED_DESC") String sort,
                              @RequestParam(value = "query", required = false) String query,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "3") int size,
                              Model model) {

        if (page < 0) {
            page = 0;
        }

        SortOption sortOption = SortOption.from(sort);
        Pageable pageable = PageRequest.of(page, size);
        Page<WordDto> wordPage = wordService.searchWords(query, sortOption.getField(), sortOption.getDirection(), pageable);

        model.addAttribute("currentSort", sortOption.name());
        model.addAttribute("words", wordPage.getContent());
        model.addAttribute("query", "");
        model.addAttribute("searchQuery", query);
        model.addAttribute("currentPage", wordPage.getNumber());
        model.addAttribute("totalPages", wordPage.getTotalPages());
        model.addAttribute("totalItems", wordPage.getTotalElements());
        return "search-page";
    }

    @GetMapping("/autocomplete")
    @ResponseBody
    public List<WordDto> autoComplete(@RequestParam("term") String term) {
        try {
            return wordService.autocomplete(term);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
