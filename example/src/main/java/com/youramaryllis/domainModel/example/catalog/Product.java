package com.youramaryllis.domainModel.example.catalog;

import com.youramaryllis.ddd.domainModel.annotations.AggregateRoot;
import com.youramaryllis.ddd.domainModel.annotations.Entity;
import com.youramaryllis.ddd.domainModel.annotations.Id;

import java.math.BigDecimal;

@AggregateRoot
@Entity
public class Product {
    @Id
    public ProductSku productSku;
    public BigDecimal price;
    public String name;
}
