package com.youramaryllis.ddd.example.order;

import com.youramaryllis.ddd.domainModel.annotations.AggregateRoot;
import com.youramaryllis.ddd.domainModel.annotations.Entity;
import com.youramaryllis.ddd.example.catalog.ProductSku;
import com.youramaryllis.ddd.example.user.UserId;

import java.util.List;

@AggregateRoot
@Entity
public class OrderService {
    private List<Order> orders;
    public void openOrder(UserId user, List<ProductSku> products) {}
}
