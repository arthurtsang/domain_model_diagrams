package com.youramaryllis.domainModel.example.catalog;

import com.youramaryllis.ddd.domainModel.annotations.AggregateRoot;
import com.youramaryllis.ddd.domainModel.annotations.DomainService;

import java.math.BigDecimal;
import java.util.List;

@AggregateRoot
@DomainService
public class CatalogService {
    List<Catalog> catalogList;

    public void createCatalog(String name){}
    public void createProduct(String catalogName, String productName, BigDecimal productPrice){}
}
