package com.backend_task.ecommerce_backend.service;

import com.backend_task.ecommerce_backend.model.Product;
import com.backend_task.ecommerce_backend.repository.ProductRepository;
import com.backend_task.ecommerce_backend.search.ProductSearch;
import com.backend_task.ecommerce_backend.search.ProductSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSearchRepository productSearchRepository;

    // Helper: safely get category name (returns null if no category)
    private String safeCategoryName(Product p) {
        if (p == null) return null;
        if (p.getCategory() == null) return null;
        return p.getCategory().getName();
    }

    // ✅ Add a single product and index it in Elasticsearch (now includes categoryName)
    public Product addProduct(Product product) {
        Product savedProduct = productRepository.save(product);

        ProductSearch searchDoc = new ProductSearch();
        searchDoc.setId(savedProduct.getProductId()); // internal ID for ES, not displayed
        searchDoc.setName(savedProduct.getName());
        searchDoc.setMrp(savedProduct.getMrp());
        searchDoc.setDiscountedPrice(savedProduct.getDiscountedPrice());
        searchDoc.setQuantity(savedProduct.getQuantity());

        // <-- NEW: set categoryName so ES can find products by category text
        searchDoc.setCategoryName(safeCategoryName(savedProduct));

        productSearchRepository.save(searchDoc);
        return savedProduct;
    }

    // ✅ Add multiple products and index them all (includes categoryName)
    public void addMultipleProducts(List<Product> products) {
        List<Product> savedProducts = productRepository.saveAll(products);

        List<ProductSearch> searchDocs = savedProducts.stream().map(p -> {
            ProductSearch ps = new ProductSearch();
            ps.setId(p.getProductId());
            ps.setName(p.getName());
            ps.setMrp(p.getMrp());
            ps.setDiscountedPrice(p.getDiscountedPrice());
            ps.setQuantity(p.getQuantity());

            // <-- NEW: include category name for each product
            ps.setCategoryName(safeCategoryName(p));

            return ps;
        }).collect(Collectors.toList());

        productSearchRepository.saveAll(searchDocs);
        System.out.println("✅ Added and indexed " + searchDocs.size() + " products.");
    }

    // ✅ Get all products (paginated)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // ✅ Get products by category (paginated)
    public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryCategoryId(categoryId, pageable);
    }

    // ✅ Reindex all products into Elasticsearch (used on startup or manually)
    public void reindexAllProducts() {
        List<Product> allProducts = productRepository.findAll();

        List<ProductSearch> allSearchDocs = allProducts.stream().map(p -> {
            ProductSearch ps = new ProductSearch();
            ps.setId(p.getProductId());
            ps.setName(p.getName());
            ps.setMrp(p.getMrp());
            ps.setDiscountedPrice(p.getDiscountedPrice());
            ps.setQuantity(p.getQuantity());

            // <-- NEW: include category name during reindex
            ps.setCategoryName(safeCategoryName(p));

            return ps;
        }).collect(Collectors.toList());

        productSearchRepository.saveAll(allSearchDocs);
        System.out.println("✅ Reindexed " + allSearchDocs.size() + " products into Elasticsearch.");
    }
}
