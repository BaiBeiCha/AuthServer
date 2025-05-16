package com.baibei.authserver.service;

import com.baibei.authserver.entity.RefreshToken;
import com.baibei.authserver.entity.User;

public interface RefreshTokenService {
    RefreshToken generateToken(User user);

    RefreshToken findByToken(String token);

    void deleteByToken(String token);
}
