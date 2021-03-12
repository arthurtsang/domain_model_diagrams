package com.youramaryllis.domainModel.example.catalog;

import com.youramaryllis.ddd.domainModel.annotations.Aggregate;
import com.youramaryllis.ddd.domainModel.annotations.Entity;
import com.youramaryllis.ddd.domainModel.annotations.Id;

import java.util.List;

@Aggregate
@Entity
public class Catalog {
    @Id
    public int catalogId;
    public String name;
    List<Product> products;

    public void addProduct(Product product){}
    public void removeProduct(ProductSku productSku){}
}
