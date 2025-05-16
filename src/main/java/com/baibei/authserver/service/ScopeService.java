package com.baibei.authserver.service;

import com.baibei.authserver.entity.Scope;

import java.util.List;

public interface ScopeService {
    Scope findByName(String name);

    Scope save(Scope role);

    boolean existsByName(String name);

    List<Scope> findAll();
}
