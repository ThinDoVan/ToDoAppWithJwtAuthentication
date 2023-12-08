package com.example.todoappwithjwtauthentication.repositories;

import com.example.todoappwithjwtauthentication.entites.PriorityLevel;
import com.example.todoappwithjwtauthentication.enums.EPriorityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriorityLevelRepository extends JpaRepository<PriorityLevel, Integer> {
    Optional<PriorityLevel> findByePriorityLevel(EPriorityLevel ePriorityLevelName);
}
