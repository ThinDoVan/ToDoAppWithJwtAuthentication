package com.example.todoappwithjwtauthentication.services;

import com.example.todoappwithjwtauthentication.dto.requests.ToDoRequest;
import com.example.todoappwithjwtauthentication.dto.responses.MessageResponse;
import com.example.todoappwithjwtauthentication.dto.responses.ToDoResponse;
import com.example.todoappwithjwtauthentication.entites.ToDo;
import com.example.todoappwithjwtauthentication.enums.ETag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ToDoServices {
    MessageResponse addNewToDo(ToDoRequest toDoRequest);

    Page<ToDoResponse> getAllToDo(Integer page, Integer size);

    Page<ToDoResponse> getUserToDo(String username, Integer page, Integer size);

    Page<ToDoResponse> getUserToDo(List<ETag> listTags, String username, Integer page, Integer size);

    ToDo getToDo(Integer toDoId);

    MessageResponse removeToDo(int toDoId);

    MessageResponse updateToDo(int toDoId, ToDoRequest toDoRequest);
}
