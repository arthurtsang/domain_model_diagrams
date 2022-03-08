package com.youramaryllis.ddd.example.order;

import com.youramaryllis.ddd.domainModel.annotations.AggregateRoot;
import com.youramaryllis.ddd.domainModel.annotations.Entity;
import com.youramaryllis.ddd.domainModel.annotations.Event;
import com.youramaryllis.ddd.example.catalog.CatalogService;
import com.youramaryllis.ddd.example.catalog.ProductSku;
import com.youramaryllis.ddd.example.payment.PaymentGateway;
import com.youramaryllis.ddd.example.user.User;
import com.youramaryllis.ddd.example.user.UserManagement;

import java.util.List;

@AggregateRoot
@Entity
public class OrderService {
    private List<Order> orders;

    @Event(value = "order opened", target = UserManagement.class, persona = "Sales")
    public OrderId openOrder(User user) {
        return null;
    }

    @Event(value = "product ordered", target = CatalogService.class)
    public void addProductToOrder(OrderId orderId, ProductSku productSku, int quantity) {
    }

    @Event( value = "order submitted", target = PaymentGateway.class)
    public void pay() {}
}
