package com.example.todoappwithjwtauthentication.repositories;

import com.example.todoappwithjwtauthentication.entites.Status;
import com.example.todoappwithjwtauthentication.enums.EStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
    Optional<Status> findByeStatus(EStatus eStatusName);
}
