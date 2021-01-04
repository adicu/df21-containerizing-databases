package com.example.adiworkshop.service;

import com.example.adiworkshop.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface CRUDService {
    Object getAllUsers();
    Object getUserEmail(String userName);
    int createNewUser(User user);
    int updateUser(User user);
    int deleteUser(Integer userId);
}
