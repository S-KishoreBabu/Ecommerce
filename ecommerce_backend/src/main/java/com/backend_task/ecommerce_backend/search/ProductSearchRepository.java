package com.backend_task.ecommerce_backend.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductSearch, Long> {

    // Custom query method to search products by name and price range
    Page<ProductSearch> findByNameContainingAndDiscountedPriceLessThan(
        String name, double maxPrice, Pageable pageable
    );
}
