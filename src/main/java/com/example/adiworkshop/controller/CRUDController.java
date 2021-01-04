package com.example.adiworkshop.controller;

import com.example.adiworkshop.service.CRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CRUDController {
    @Autowired
    private CRUDService service;

    /**
     * A Simple Example: Listens for the GET request with path /test
     * @return a String "DevFest 2021"
     */
    @GetMapping("/test")
    public String testAPI() {
        return "DevFest 2021";
    }

    @GetMapping("/getAllUsers")
    public Object getAllUsers() {
        return this.service.getAllUsers();
    }
}
