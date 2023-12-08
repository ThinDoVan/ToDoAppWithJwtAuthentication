package com.example.todoappwithjwtauthentication.repositories;

import com.example.todoappwithjwtauthentication.entites.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Integer> {
}
