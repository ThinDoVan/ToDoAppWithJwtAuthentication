package com.example.todoappwithjwtauthentication.security.services;

import com.example.todoappwithjwtauthentication.entites.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {
    private Integer id;
    private String username;
    private String fullName;
    private String email;
    @JsonIgnore
    private String password;

    private GrantedAuthority authorities;

    public UserDetailsImpl(Integer id, String username, String fullName, String email, String password, GrantedAuthority authorities) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user){
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleName().name());
        return new UserDetailsImpl(user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                authority);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(authorities);
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj){
            return true;
        }if (obj==null||getClass()!=obj.getClass()){
            return false;
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) obj;
        return Objects.equals(id, userDetails.id);
    }
}
