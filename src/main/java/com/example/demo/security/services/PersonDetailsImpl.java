package com.example.demo.security.services;

import com.example.demo.models.Person;
import com.example.demo.models.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PersonDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private String password;
    private  Collection<? extends GrantedAuthority> authorities;

    public PersonDetailsImpl(long id, String firstName, String lastName, String email, String password,
                             Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static PersonDetailsImpl build(Person person) {
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(person.getRole().getDescription()));

        return new PersonDetailsImpl(person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getEmail(),
                person.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PersonDetailsImpl personDetails = (PersonDetailsImpl) o;
        return Objects.equals(id, personDetails.id);
    }
}
