package com.youramaryllis.ddd.example.catalog;

import com.youramaryllis.ddd.domainModel.annotations.Entity;
import com.youramaryllis.ddd.domainModel.annotations.Id;

import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    public ProductSku productSku;
    public BigDecimal price;
    public String name;
}
