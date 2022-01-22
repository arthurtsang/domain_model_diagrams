package com.youramaryllis.ddd.example.user;

import com.youramaryllis.ddd.domainModel.annotations.Entity;
import com.youramaryllis.ddd.domainModel.annotations.Id;

import java.util.List;

@Entity
public class User {
    @Id
    public UserId userId;
    public String name;
    public List<PaymentInfo> paymentInfos;
    public List<DeliveryInfo> deliveryInfos;
}
