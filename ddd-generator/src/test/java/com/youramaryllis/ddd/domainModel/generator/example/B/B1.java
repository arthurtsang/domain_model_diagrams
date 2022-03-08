package com.youramaryllis.ddd.domainModel.generator.example.B;

import com.youramaryllis.ddd.domainModel.annotations.AggregateRoot;
import com.youramaryllis.ddd.domainModel.annotations.Entity;
import com.youramaryllis.ddd.domainModel.annotations.Event;
import com.youramaryllis.ddd.domainModel.annotations.Id;
import com.youramaryllis.ddd.domainModel.generator.example.A.A1;
import com.youramaryllis.ddd.domainModel.generator.example.A.A3;

@AggregateRoot
@Entity
public class B1 {
    @Id
    String id;
    String text2;

    @Event(value = "get a1", target = A1.class)
    @Event(value = "get a3", target = A3.class)
    A1 getA1(){ return null;}
}
