package com.example.mls_project.Entities;

public class User {
    private final String fullName, email, admin, status;

    public User(String fullName, String email, String admin, String status) {
        this.fullName = fullName;
        this.email = email;
        this.admin = admin;
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getAdmin() {
        return admin;
    }

    public String getStatus() {
        return status;
    }
}
