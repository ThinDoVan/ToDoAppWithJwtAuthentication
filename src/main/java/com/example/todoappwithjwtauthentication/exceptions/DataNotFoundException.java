package com.example.todoappwithjwtauthentication.exceptions;

import com.example.todoappwithjwtauthentication.dto.responses.MessageResponse;

public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(String message) {
        super(message);
    }
}
