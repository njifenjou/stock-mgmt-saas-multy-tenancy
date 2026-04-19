package com.jenkins.saas.service.impl;

import com.jenkins.saas.common.PageResponse;
import com.jenkins.saas.entity.Category;
import com.jenkins.saas.entity.Product;
import com.jenkins.saas.exceptions.DuplicateResourceException;
import com.jenkins.saas.mappers.ProductMapper;
import com.jenkins.saas.repositories.CategoryRepository;
import com.jenkins.saas.repositories.ProductRepository;
import com.jenkins.saas.requests.ProductRequest;
import com.jenkins.saas.responses.ProductResponse;
import com.jenkins.saas.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public void create(final ProductRequest request) {
        CheckIfProductExistsByReference(request.getReference());
        CheckIfCategoryExistsById(request.getCategoryId());
        final Product entity = productMapper.toEntity(request);
        this.productRepository.save(entity);

    }

    @Override
    public void update(final String id,final ProductRequest request) {
        final Optional<Product> productExist = this.productRepository.findById(id);
        if (productExist.isEmpty()) {
            log.debug("Product does not exist");
            throw new EntityNotFoundException("Product does not exist");
        }
        CheckIfProductExistsByReference(request.getReference());
        CheckIfCategoryExistsById(request.getCategoryId());

        final Product productToUpdate = this.productMapper.toEntity(request);
        productToUpdate.setId(id);
        this.productRepository.save(productToUpdate);
    }

    @Override
    public PageResponse<ProductResponse> findAll(final int page,final int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<Product> products = this.productRepository.findAll(pageRequest);
        final Page<ProductResponse> productResponses = products.map(productMapper::toResponse);
        return PageResponse.of(productResponses);
    }


    @Override
    public ProductResponse findById(final String id) {
        return this.productRepository.findById(id)
                .map(this.productMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product does not exist"));
    }

    @Override
    public void delete(final String id) {
        final Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product does not exist"));
        this.productRepository.delete(product);


    }

    private void CheckIfProductExistsByReference(final String reference) {
        final Optional<Product> product = productRepository.findByReferenceIgnoreCase(reference);
        if (product.isPresent()) {
            log.debug("Product already exists");
            throw new DuplicateResourceException("Product already exists");
        }


    }

    private void CheckIfCategoryExistsById(final String categoryId) {
        final Optional<Category> category = this.categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            log.debug("Category does not exists");
            throw new EntityNotFoundException("Category does not exists");
        }
    }
}
