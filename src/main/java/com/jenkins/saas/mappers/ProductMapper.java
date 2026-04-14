package com.jenkins.saas.mappers;

import com.jenkins.saas.entity.Category;
import com.jenkins.saas.entity.Product;
import com.jenkins.saas.requests.ProductRequest;
import com.jenkins.saas.responses.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(final ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .reference(request.getReference())
                .description(request.getDescription())
                .price(request.getPrice())
                .alertStock(Integer.valueOf(request.getAlertStock()))
                .category(Category.builder().id(request.getCategoryId()).build())
                .build();
    }

    public ProductResponse toResponse(final Product product) {
        return ProductResponse.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .reference(product.getReference())
                .categoryName(product.getCategory().getName())

                .build();
    }
}
