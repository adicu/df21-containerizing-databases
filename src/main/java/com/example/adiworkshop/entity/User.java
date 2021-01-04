package com.example.adiworkshop.entity;

/**
 * DTO (data transfer object) for the Users table
 */
public class User {
    /**
     * Variable name has to be the same as column name of the Users table
     */
    private String userId;
    private String userName;
    private String userEmail;
    private String userRole;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
