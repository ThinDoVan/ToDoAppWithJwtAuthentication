package com.example.todoappwithjwtauthentication.services;

import com.example.todoappwithjwtauthentication.dto.requests.LoginRequest;
import com.example.todoappwithjwtauthentication.dto.requests.SignupRequest;
import com.example.todoappwithjwtauthentication.dto.responses.JwtResponse;
import com.example.todoappwithjwtauthentication.dto.responses.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface UserServices {
    ResponseEntity<JwtResponse> signIn(LoginRequest loginRequest);
    ResponseEntity<MessageResponse> register(SignupRequest signupRequest);
}
