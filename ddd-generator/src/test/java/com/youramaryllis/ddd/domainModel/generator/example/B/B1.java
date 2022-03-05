package com.youramaryllis.ddd.domainModel.generator.example.B;

import com.youramaryllis.ddd.domainModel.annotations.*;
import com.youramaryllis.ddd.domainModel.generator.example.A.A1;

@AggregateRoot
@Entity
public class B1 {
    @Id
    String id;
    String text2;
    @CrossBoundaryReference(A1.class)
    @Event("get a1")
    A1 getA1(){ return null;}
}
