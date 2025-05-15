package com.example.nihongo_trainer.controller;

import com.example.nihongo_trainer.dto.CategoryDto;
import com.example.nihongo_trainer.entity.Category;
import com.example.nihongo_trainer.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String showCategories(Model model) {
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "category-list";
    }

    @PostMapping
    public String createCategory(@RequestParam String name, RedirectAttributes redirectAttributes) {
        categoryService.saveCategory(new Category(name));
        redirectAttributes.addFlashAttribute("success", "Category was created.");
        return "redirect:/categories";
    }

    @PostMapping("/{id}")
    public String updateCategory(@PathVariable Long id,
                                 @RequestParam String name,
                                 RedirectAttributes redirectAttributes) {
        categoryService.updateCategory(id, new CategoryDto(id, name));
        redirectAttributes.addFlashAttribute("success", "Category was updated.");
        return "redirect:/categories";
    }

    @PostMapping(value = "/{id}", params = "_method=delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("success", "Category was deleted.");
        return "redirect:/categories";
    }
}
