package com.example.todoappwithjwtauthentication.services;

import com.example.todoappwithjwtauthentication.dto.requests.ToDoRequest;
import com.example.todoappwithjwtauthentication.dto.responses.MessageResponse;
import com.example.todoappwithjwtauthentication.entites.ToDo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ToDoServices {
    ResponseEntity<MessageResponse> addNewToDo(ToDoRequest toDoRequest);
    ResponseEntity<List<ToDo>> getAllToDo(Integer page, Integer size);
    ResponseEntity<?> getUserToDo(Integer userId, Integer page, Integer size);
    ResponseEntity<?> getToDo(Integer toDoId);
    ResponseEntity<MessageResponse> removeToDo(int toDoId);
    ResponseEntity<MessageResponse> updateToDo(int toDoId, ToDoRequest toDoRequest);
}
