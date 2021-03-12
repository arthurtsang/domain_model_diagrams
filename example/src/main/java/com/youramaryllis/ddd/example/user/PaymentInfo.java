package com.youramaryllis.ddd.example.user;

import com.youramaryllis.ddd.domainModel.annotations.ValueObject;

@ValueObject
public class PaymentInfo {
    public String name;
    public String cardNumber;
    public Address billingAddress;
}
