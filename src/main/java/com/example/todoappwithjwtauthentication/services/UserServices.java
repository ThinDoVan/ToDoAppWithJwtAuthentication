package com.example.todoappwithjwtauthentication.services;

import com.example.todoappwithjwtauthentication.dto.requests.LoginRequest;
import com.example.todoappwithjwtauthentication.dto.requests.SignupRequest;
import com.example.todoappwithjwtauthentication.dto.responses.JwtResponse;
import com.example.todoappwithjwtauthentication.dto.responses.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface UserServices {
    JwtResponse signIn(LoginRequest loginRequest);
    MessageResponse register(SignupRequest signupRequest);
}
