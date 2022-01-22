package com.youramaryllis.ddd.example.order;

import com.youramaryllis.ddd.domainModel.annotations.AggregateRoot;
import com.youramaryllis.ddd.domainModel.annotations.CrossBoundaryReference;
import com.youramaryllis.ddd.domainModel.annotations.Entity;
import com.youramaryllis.ddd.domainModel.annotations.Event;
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
    @Event(value = "order opened", persona = "Sales")
    public OrderId openOrder(User user) {
        return null;
    }

    @CrossBoundaryReference(CatalogService.class)
    @Event(value = "product ordered", persona = "Customer")
    public void addProductToOrder(OrderId orderId, ProductSku productSku, int quantity) {
    }
}
