package com.example.adiworkshop.service;

import com.example.adiworkshop.entity.User;
import com.example.adiworkshop.mapper.CRUDMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CRUDServiceImpl implements CRUDService{
    @Autowired
    private CRUDMapper mapper;

    @Override
    public Object getAllUsers() {
        return this.mapper.getAllUsers();
    }

    @Override
    public Object getUserEmail(String userName) {
        return this.mapper.getUserEmail(userName);
    }

    @Override
    public int createNewUser(User user) {
        return this.mapper.createNewUser(user);
    }

    @Override
    public int updateUser(User user) {
        return this.mapper.updateUser(user);
    }

    @Override
    public int deleteUser(Integer userId) {
        return this.mapper.deleteUser(userId);
    }
}
