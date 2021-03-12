package com.youramaryllis.ddd.example.order;

import com.youramaryllis.ddd.domainModel.annotations.AggregateRoot;
import com.youramaryllis.ddd.domainModel.annotations.CrossBoundaryReference;
import com.youramaryllis.ddd.domainModel.annotations.Entity;
import com.youramaryllis.ddd.example.catalog.Product;
import com.youramaryllis.ddd.example.user.User;

import java.util.List;

@AggregateRoot
@Entity
public class OrderService {
    private List<Order> orders;

    @CrossBoundaryReference(User.class)
    public OrderId openOrder(User user) {
        return null;
    }

    @CrossBoundaryReference(Product.class)
    public void addProductToOrder(OrderId orderId, Product productSku, int quantity) {
    }
}
