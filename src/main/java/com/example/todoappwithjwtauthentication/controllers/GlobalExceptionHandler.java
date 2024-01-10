package com.example.todoappwithjwtauthentication.controllers;

import com.example.todoappwithjwtauthentication.dto.responses.MessageResponse;
import com.example.todoappwithjwtauthentication.exceptions.DataNotFoundException;
import com.example.todoappwithjwtauthentication.exceptions.InvalidDataException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({DataNotFoundException.class,
            UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public MessageResponse handleDataNotFoundException(RuntimeException ex) {
        MessageResponse errorResponse = new MessageResponse();
        errorResponse.setMessage(ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler({InvalidDataException.class,
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MessageResponse handleInvalidDataException(RuntimeException ex) {
        MessageResponse errorResponse = new MessageResponse();
        errorResponse.setMessage(ex.getMessage());
        return errorResponse;
    }
}
