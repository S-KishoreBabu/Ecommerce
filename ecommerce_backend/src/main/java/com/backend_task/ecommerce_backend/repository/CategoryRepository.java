package com.backend_task.ecommerce_backend.repository;

import com.backend_task.ecommerce_backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
