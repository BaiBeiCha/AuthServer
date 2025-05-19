package com.baibei.authserver.service;

import com.baibei.authserver.entity.Role;

import java.util.List;

public interface RoleService {
    Role findByName(String name);

    List<Role> findAll();

    Role save(Role role);

    boolean existsByName(String name);

    void delete(Role role);
}
