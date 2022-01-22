package com.youramaryllis.ddd.example.user;

import com.youramaryllis.ddd.domainModel.annotations.AggregateRoot;
import com.youramaryllis.ddd.domainModel.annotations.DomainService;

import java.util.List;

@AggregateRoot
@DomainService
public class UserManagement {
    List<User> user;
    UserId test;

    public UserId addUser(String name){ return null; }
    public void removeUser(UserId userId){}
    public void updateUser(UserId userId, String newName){}
    public List<User> listUsers(){ return null; }
    public List<User> searchUsers(String criteria){ return null; }
    public User getUser(UserId userId){ return null; }
}
