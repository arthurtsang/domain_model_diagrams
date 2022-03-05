package com.youramaryllis.ddd.domainModel.generator.example.A;

import com.youramaryllis.ddd.domainModel.annotations.AggregateRoot;
import com.youramaryllis.ddd.domainModel.annotations.Entity;
import com.youramaryllis.ddd.domainModel.annotations.Id;

@AggregateRoot @Entity
public class A1 {
    @Id String id;
    String text1;
    A2 a2;
}
