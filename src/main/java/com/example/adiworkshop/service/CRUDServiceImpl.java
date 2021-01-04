package com.example.adiworkshop.service;

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
}
