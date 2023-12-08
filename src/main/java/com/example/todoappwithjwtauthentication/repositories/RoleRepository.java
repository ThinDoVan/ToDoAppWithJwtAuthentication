package com.example.todoappwithjwtauthentication.repositories;

import com.example.todoappwithjwtauthentication.entites.Role;
import com.example.todoappwithjwtauthentication.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByeRole(ERole eRoleName);
}
