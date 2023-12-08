package com.example.todoappwithjwtauthentication.dto.requests;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pasword) {
        this.password = pasword;
    }
}
