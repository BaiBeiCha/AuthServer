package com.baibei.authserver.service;

import com.baibei.authserver.entity.User;

public interface UserService {
    User findByUsername(String username);
    User save(User user);

    boolean existsByUsername(String username);
}
