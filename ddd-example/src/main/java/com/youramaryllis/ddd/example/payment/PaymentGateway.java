package com.youramaryllis.ddd.example.payment;

import com.youramaryllis.ddd.domainModel.annotations.AggregateRoot;
import com.youramaryllis.ddd.domainModel.annotations.External;

@External
@AggregateRoot
public class PaymentGateway {

    public void creditCard() {}
}
