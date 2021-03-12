package com.youramaryllis.domainModel.example.order;

import com.youramaryllis.domainModel.example.catalog.ProductSku;
import com.youramaryllis.ddd.domainModel.annotations.Entity;
import com.youramaryllis.ddd.domainModel.annotations.Id;

import java.math.BigDecimal;

@Entity
public class LineItem {
    @Id
    public String lineItemId;
    public ProductSku productSku;
    public int quantity;
    public BigDecimal unitPrice;
}
