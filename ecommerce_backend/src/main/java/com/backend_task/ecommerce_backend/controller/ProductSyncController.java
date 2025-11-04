package com.backend_task.ecommerce_backend.controller;

import com.backend_task.ecommerce_backend.model.Product;
import com.backend_task.ecommerce_backend.repository.ProductRepository;
import com.backend_task.ecommerce_backend.search.ProductSearch;
import com.backend_task.ecommerce_backend.search.ProductSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sync")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductSyncController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @PostMapping("/products")
    public String syncProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            return "⚠️ No products found in database.";
        }

        List<ProductSearch> productSearchList = products.stream().map(product -> {
            ProductSearch ps = new ProductSearch();
            ps.setId(product.getProductId()); // Match field name from Product entity
            ps.setName(product.getName());
            ps.setMrp(product.getMrp());
            ps.setDiscountedPrice(product.getDiscountedPrice());
            ps.setQuantity(product.getQuantity());
            return ps;
        }).collect(Collectors.toList());

        productSearchRepository.saveAll(productSearchList);

        return "✅ " + productSearchList.size() + " products synced to Elasticsearch.";
    }
}
