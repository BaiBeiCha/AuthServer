package com.baibei.authserver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "scopes")
@Data
public class Scope {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public Scope() {}

    public Scope(String name) {
        this.name = name;
    }

    public static String scope(String name) {
        return "SCOPE_" + name.toUpperCase();
    }

    public static String generateScope(User user) {
        StringBuilder scopeBuilder = new StringBuilder();
        user.getScopes().forEach((scope -> scopeBuilder.append(scope.getName()).append(",")));
        return scopeBuilder.deleteCharAt(scopeBuilder.length() - 1).toString();
    }
}
