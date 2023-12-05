package com.example.todoappwithjwtauthentication.entites;

import com.example.todoappwithjwtauthentication.enums.ERole;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole eRole;

    public Role() {
    }

    public Role(ERole ERole) {
        this.eRole = ERole;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public ERole getRoleName() {
        return eRole;
    }

    public void setRoleName(ERole eRole) {
        this.eRole = eRole;
    }
}
