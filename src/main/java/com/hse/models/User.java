package com.hse.models;

import com.hse.enums.Specialization;
import com.hse.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Data
@NoArgsConstructor
public class User implements UserDetails {

    private long id;
    private UserRole userRole;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String username;
    private String password;
    private Specialization specialization;
    private float rating;
    private String description;
    private List<String> images;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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

}