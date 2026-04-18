package com.jenkins.saas.service.impl;

import com.jenkins.saas.common.PageResponse;
import com.jenkins.saas.entity.Category;
import com.jenkins.saas.mappers.CategoryMapper;
import com.jenkins.saas.repositories.CategoryRepository;
import com.jenkins.saas.requests.CategoryRequest;
import com.jenkins.saas.responses.CategoryResponse;
import com.jenkins.saas.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    @Override
    public void create(final CategoryRequest request) {
        CheckIfCategoryAlreadyExistsByName(request.getName());
        final Category entity = this.categoryMapper.toEntity(request);
        this.categoryRepository.save(entity);
    }


    @Override
    public void update(final String id,final CategoryRequest request) {
        final Optional<Category> category = this.categoryRepository.findById(id);
        if (category.isEmpty()) {
            log.debug("Category does not exist.");
            throw new EntityNotFoundException("Category does not exist");

        }

        CheckIfCategoryAlreadyExistsByName(request.getName());
        final Category categoryToUpdate = this.categoryMapper.toEntity(request);
        categoryToUpdate.setId(id);
        this.categoryRepository.save(categoryToUpdate);
    }

    @Override
    public PageResponse<CategoryResponse> findAll(final int page, final int size) {
       final PageRequest pageRequest = PageRequest.of(page, size);
       final Page<Category> categories = this.categoryRepository.findAll(pageRequest);
       final Page<CategoryResponse> categoryResponses = categories.map(categoryMapper::toResponse);

        return PageResponse.of(categoryResponses);
    }

    @Override
    public CategoryResponse findById(final String id) {
        return this.categoryRepository.findById(id)
                .map(categoryMapper::toResponse)
                .orElseThrow(()-> new EntityNotFoundException("Category does not exist"));

    }

    @Override
    public void delete(final String id) {
        this.categoryRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Category does not exist"));
        this.categoryRepository.deleteById(id);
    }

    private void CheckIfCategoryAlreadyExistsByName(final String categoryName) {
        final Optional<Category> category = this.categoryRepository.findByNameIgnoreCase(categoryName);
        if (category.isPresent()) {
            log.debug("Category already exists.");
            throw new RuntimeException("Category already exists");
        }

    }
}
