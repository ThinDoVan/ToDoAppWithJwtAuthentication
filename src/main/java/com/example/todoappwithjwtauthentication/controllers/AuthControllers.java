package com.example.todoappwithjwtauthentication.controllers;

import com.example.todoappwithjwtauthentication.dto.requests.LoginRequest;
import com.example.todoappwithjwtauthentication.dto.requests.SignupRequest;
import com.example.todoappwithjwtauthentication.dto.responses.JwtResponse;
import com.example.todoappwithjwtauthentication.dto.responses.MessageResponse;
import com.example.todoappwithjwtauthentication.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthControllers {

    @Autowired
    private UserServices userServices;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        JwtResponse responseBody = userServices.signIn(loginRequest)
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest){
        MessageResponse message = userServices.register(signupRequest);
        return ResponseEntity.ok(message);
    }

}
