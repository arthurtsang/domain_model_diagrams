package com.youramaryllis.domainModel.example.order;

import com.youramaryllis.ddd.domainModel.annotations.ValueObject;

@ValueObject
public class Address {
    public String street;
    public String city;
    public String state;
    public String zip;
}
