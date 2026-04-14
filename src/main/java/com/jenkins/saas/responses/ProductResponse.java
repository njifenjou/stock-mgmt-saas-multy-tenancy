package com.jenkins.saas.responses;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private String name;
    private String reference;
    private String description;
    private String alertStock;
    private BigDecimal price;
    private String categoryName;
    private int availableQuantity;
}
