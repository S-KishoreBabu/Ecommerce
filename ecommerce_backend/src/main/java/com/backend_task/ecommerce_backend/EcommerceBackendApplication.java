package com.backend_task.ecommerce_backend;

import com.backend_task.ecommerce_backend.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.backend_task.ecommerce_backend.repository")
@EnableElasticsearchRepositories(basePackages = "com.backend_task.ecommerce_backend.search")
public class EcommerceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceBackendApplication.class, args);
    }

    // ✅ Automatically reindex all products on startup
    @Bean
    public CommandLineRunner init(ProductService productService) {
        return args -> {
            productService.reindexAllProducts();
        };
    }
}
