package com.jenkins.saas.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {
    @NotBlank(message = "category name should not be empty")
    @Size(min = 3, max = 255, message = "Category name should be betwen 3 and 255 characters")
    private String name;
    @NotBlank(message = "category name should not be empty")
    private String description;
}
