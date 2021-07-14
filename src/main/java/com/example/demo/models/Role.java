package com.example.demo.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    public Role() {
    }
    public Role(RoleEnum name) {
        this.description = name.toString();
    }

    public Role(String description) {
        this.description = description;
    }
}

