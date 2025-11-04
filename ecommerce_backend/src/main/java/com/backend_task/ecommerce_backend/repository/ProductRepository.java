package com.backend_task.ecommerce_backend.repository;

import com.backend_task.ecommerce_backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryCategoryId(Long categoryId, Pageable pageable);
}
