package com.baibei.authserver.service;

import com.baibei.authserver.entity.User;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    User save(User user);

    boolean existsByUsername(String username);

    void delete(User user);

    List<User> findAll();
}
