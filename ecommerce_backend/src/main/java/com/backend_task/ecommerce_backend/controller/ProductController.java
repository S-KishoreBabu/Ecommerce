package com.backend_task.ecommerce_backend.controller;

import com.backend_task.ecommerce_backend.model.Product;
import com.backend_task.ecommerce_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ✅ Add multiple products and index them in Elasticsearch
    @PostMapping("/bulk")
    public ResponseEntity<?> addMultipleProducts(@RequestBody List<Product> products) {
        try {
            productService.addMultipleProducts(products);
            return ResponseEntity.ok("All products added and indexed successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding products: " + e.getMessage());
        }
    }

    // ✅ Get all products with pagination
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    // ✅ Get products by category (paginated)
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<Product>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(products);
    }
}
