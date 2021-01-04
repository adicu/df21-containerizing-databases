package com.example.adiworkshop.controller;

import com.example.adiworkshop.entity.User;
import com.example.adiworkshop.service.CRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getUserEmail")
    public Object getUserEmail(@RequestParam String userName){
        return this.service.getUserEmail(userName);
    }

    @PostMapping("/createNewUser")
    public String createNewUser(User user){
        return this.service.createNewUser(user) == 1? "success":"failed";
    }

    @PostMapping("/updateUser")
    public String updateUser(User user){
        return this.service.updateUser(user) == 1? "success":"failed";
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam Integer userId){
        return this.service.deleteUser(userId) == 1? "success":"failed";
    }
}
