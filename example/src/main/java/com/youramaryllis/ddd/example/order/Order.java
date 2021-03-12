package com.youramaryllis.ddd.example.order;

import com.youramaryllis.ddd.domainModel.annotations.Entity;
import com.youramaryllis.ddd.domainModel.annotations.Id;
import com.youramaryllis.ddd.example.user.UserId;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
public class Order {
    @Id
    public OrderId orderId;
    public List<LineItem> lineItems;
    public Date orderDate;
    public UserId userId;
    public PaymentInfo paymentInfo;
    public DeliveryInfo deliveryInfo;
    public BigDecimal totalPrice;

    public Order(UserId userId, PaymentInfo paymentInfo, DeliveryInfo deliveryInfo) {
    }
}
