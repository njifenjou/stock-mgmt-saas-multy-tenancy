package com.jenkins.saas.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "Product name should not be empty")
    @Size(min = 3, max = 255, message = "Product name should be betwen 3 and 255 characters")
    private String name;
    @NotBlank(message = "description name should not be empty")
    private String description;
    @NotBlank(message = "reference name should not be empty")
    private String reference;
    @Positive(message = "alert stock should be a positive number")
    private String alertStock;
    @Positive(message = "price should be a positive number")
    private BigDecimal price;
    @NotBlank(message = "category ID name should not be empty")
    private String categoryId;
}
