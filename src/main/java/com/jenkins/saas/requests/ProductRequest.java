package com.jenkins.saas.requests;

import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    private String name;
    private String description;
    private String reference;
    private String alertStock;
    private BigDecimal price;
    private String categoryId;
}
