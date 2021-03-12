package com.youramaryllis.ddd.example.user;

import com.youramaryllis.ddd.domainModel.annotations.Entity;
import com.youramaryllis.ddd.domainModel.annotations.Id;

@Entity
public class User {
    @Id
    public UserId userId;
    public String name;

}
