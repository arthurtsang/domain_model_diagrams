package com.youramaryllis.ddd.example.order;

import com.youramaryllis.ddd.domainModel.annotations.AggregateRoot;
import com.youramaryllis.ddd.domainModel.annotations.CrossBoundaryReference;
import com.youramaryllis.ddd.domainModel.annotations.Entity;
import com.youramaryllis.ddd.example.catalog.CatalogService;
import com.youramaryllis.ddd.example.catalog.ProductSku;
import com.youramaryllis.ddd.example.user.User;
import com.youramaryllis.ddd.example.user.UserManagement;

import java.util.List;

@AggregateRoot
@Entity
public class OrderService {
    private List<Order> orders;

    @CrossBoundaryReference(UserManagement.class)
    public OrderId openOrder(User user) {
        return null;
    }

    @CrossBoundaryReference(CatalogService.class)
    public void addProductToOrder(OrderId orderId, ProductSku productSku, int quantity) {
    }
}
