package com.jenkins.saas.repositories;

import com.jenkins.saas.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByReferenceIgnoreCase(String reference);
}
