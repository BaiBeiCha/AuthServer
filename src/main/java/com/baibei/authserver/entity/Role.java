package com.baibei.authserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public static String role(String name) {
        return "ROLE_" + name.toUpperCase();
    }

    public static String[] roles(String... names) {
        String[] roles = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            roles[i] = role(names[i]);
        }
        return roles;
    }

    @Override
    public String toString() {
        return role(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return name.equalsIgnoreCase(role.name);
    }
}
