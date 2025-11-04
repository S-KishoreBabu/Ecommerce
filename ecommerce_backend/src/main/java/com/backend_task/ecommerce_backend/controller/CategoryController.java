package com.backend_task.ecommerce_backend.controller;

import com.backend_task.ecommerce_backend.model.Category;
import com.backend_task.ecommerce_backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:3000") // ✅ allow frontend access
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // ✅ Get all categories
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // ✅ Add new category (optional)
    @PostMapping
    public Category addCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }
}
