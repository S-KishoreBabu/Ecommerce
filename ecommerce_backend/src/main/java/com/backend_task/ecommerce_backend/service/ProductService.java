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

    // ✅ Add a single product and index it in Elasticsearch (excluding category & ID)
    public Product addProduct(Product product) {
        Product savedProduct = productRepository.save(product);

        ProductSearch searchDoc = new ProductSearch();
        searchDoc.setId(savedProduct.getProductId()); // internal ID for ES, not displayed
        searchDoc.setName(savedProduct.getName());
        searchDoc.setMrp(savedProduct.getMrp());
        searchDoc.setDiscountedPrice(savedProduct.getDiscountedPrice());
        searchDoc.setQuantity(savedProduct.getQuantity());

        productSearchRepository.save(searchDoc);
        return savedProduct;
    }

    // ✅ Add multiple products and index them all
    public void addMultipleProducts(List<Product> products) {
        List<Product> savedProducts = productRepository.saveAll(products);

        List<ProductSearch> searchDocs = savedProducts.stream().map(p -> {
            ProductSearch ps = new ProductSearch();
            ps.setId(p.getProductId());
            ps.setName(p.getName());
            ps.setMrp(p.getMrp());
            ps.setDiscountedPrice(p.getDiscountedPrice());
            ps.setQuantity(p.getQuantity());
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
            return ps;
        }).collect(Collectors.toList());

        productSearchRepository.saveAll(allSearchDocs);
        System.out.println("✅ Reindexed " + allSearchDocs.size() + " products into Elasticsearch.");
    }
}
