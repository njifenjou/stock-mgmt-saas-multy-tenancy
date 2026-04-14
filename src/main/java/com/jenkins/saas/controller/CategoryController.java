package com.jenkins.saas.controller;

import com.jenkins.saas.common.PageResponse;
import com.jenkins.saas.requests.CategoryRequest;
import com.jenkins.saas.responses.CategoryResponse;
import com.jenkins.saas.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Void> createCategory(
            @RequestBody @Valid final CategoryRequest request) {
        this.categoryService.create(request);
        return ResponseEntity.ok().build();

    }

    @PutMapping("/category-id")
    public ResponseEntity<Void> updateCategory(
            @RequestBody @Valid final CategoryRequest request,
            @RequestParam("category-id") final String id) {
        this.categoryService.update(id, request);
        return ResponseEntity.accepted().build();

    }

    @GetMapping("/category-id")
    public ResponseEntity<CategoryResponse> findCategoryById(
            @RequestParam("category-id")
            @NotNull(message = "category ID cannot be null")

            final String id) {
        return ResponseEntity.ok(this.categoryService.findById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<CategoryResponse>> findAllCategories(
            @RequestParam(name = "page", defaultValue = "0") final int page,
            @RequestParam(name = "size", defaultValue = "10") final int size
    ) {
        return ResponseEntity.ok(this.categoryService.findAll(page, size));
    }

    @DeleteMapping("/category-id")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable("category-id")
            @NotNull(message = "category ID cannot be null")
            final String id){
        this.categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
