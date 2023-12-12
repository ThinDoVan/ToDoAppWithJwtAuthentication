package com.example.todoappwithjwtauthentication.controllers;

import com.example.todoappwithjwtauthentication.dto.requests.ToDoRequest;
import com.example.todoappwithjwtauthentication.dto.responses.MessageResponse;
import com.example.todoappwithjwtauthentication.dto.responses.ToDoResponse;
import com.example.todoappwithjwtauthentication.enums.ETag;
import com.example.todoappwithjwtauthentication.services.ToDoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/todo")
public class TodoControllers {
    @Autowired
    ToDoServices toDoServices;

    @PostMapping(value = "/ThemToDo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageResponse> addToDo(@RequestBody ToDoRequest toDoRequest) {
        return toDoServices.addNewToDo(toDoRequest);
    }

    @GetMapping(value = "/admin/LayToanBoDanhSachToDo")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ToDoResponse>> getListToDo(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                          @RequestParam(required = false, defaultValue = "5") Integer size) {
        return toDoServices.getAllToDo(page, size);
    }

    @GetMapping(value = "/LayDanhSachToDoNguoiDung")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<?> getListToDoByUsername(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestParam(required = false, defaultValue = "0") Integer page,
                                                   @RequestParam(required = false, defaultValue = "5") Integer size) {
        String username = userDetails.getUsername();
            return toDoServices.getUserToDo(username, page, size);

    }

    @GetMapping(value = "/LayToDo")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<?> getToDoById(@RequestParam Integer toDoId) {
        return toDoServices.getToDo(toDoId);
    }

    @PutMapping(value = "/SuaToDo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageResponse> updateToDo(@RequestParam int toDoId,
                                                      @RequestBody ToDoRequest toDoRequest) {
        return toDoServices.updateToDo(toDoId, toDoRequest);
    }

    @DeleteMapping(value = "/XoaToDo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageResponse> deleteToDo(@RequestParam int toDoId) {
        return toDoServices.removeToDo(toDoId);
    }
}
