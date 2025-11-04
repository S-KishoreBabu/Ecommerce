package com.backend_task.ecommerce_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend_task.ecommerce_backend.model.User;

// This interface connects our app to the "users" table in MySQL
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository already provides CRUD methods like:
    // save(), findAll(), findById(), deleteById(), etc.

    // You can even define custom finders:
    Optional<User> findByEmail(String email);
}
