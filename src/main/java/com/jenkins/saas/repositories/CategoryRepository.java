package com.jenkins.saas.repositories;

import com.jenkins.saas.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {
Optional<Category> findByNameIgnoreCase(String name);
}
