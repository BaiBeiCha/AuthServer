package com.baibei.authserver.service;

import com.baibei.authserver.entity.Role;

public interface RoleService {
    Role findByName(String name);

    Role save(Role role);

    boolean existsByName(String name);
}
