package com.jenkins.saas.mappers;

import com.jenkins.saas.entity.Category;
import com.jenkins.saas.requests.CategoryRequest;
import com.jenkins.saas.responses.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public Category toEntity(final CategoryRequest request){
        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public CategoryResponse toResponse(final Category entity){
       final int nbProducts = 0;//entity.getProducts()==null ? 0 : entity.getProducts().size();
        return CategoryResponse.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .nbProducts(nbProducts)
                .build();

    }

}

