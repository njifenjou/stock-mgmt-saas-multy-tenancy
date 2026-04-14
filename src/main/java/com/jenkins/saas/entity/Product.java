package com.jenkins.saas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends AbstractEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "reference", nullable = false, unique = true)
    private String reference;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "alert_stock", nullable = false)
    private Integer alertStock;

    @Column(name = "price", nullable = false)
    private BigDecimal price;


@ManyToOne
@JoinColumn(name = "category_id")
private Category category;

@OneToMany(mappedBy = "product")
private List<StockMvt> stockMvts;



}
