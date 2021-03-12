package com.youramaryllis.domainModel.example.order;

import com.youramaryllis.domainModel.example.user.UserId;
import com.youramaryllis.ddd.domainModel.annotations.AggregateRoot;
import com.youramaryllis.ddd.domainModel.annotations.Entity;
import com.youramaryllis.ddd.domainModel.annotations.Id;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@AggregateRoot
public class Order {
    @Id
    public OrderId orderId;
    public List<LineItem> lineItems;
    public Date orderDate;
    public UserId userId;
    public PaymentInfo paymentInfo;
    public DeliveryInfo deliveryInfo;
    public BigDecimal totalPrice;
}
