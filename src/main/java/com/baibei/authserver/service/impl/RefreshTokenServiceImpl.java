package com.baibei.authserver.service.impl;

import com.baibei.authserver.entity.RefreshToken;
import com.baibei.authserver.entity.User;
import com.baibei.authserver.exception.TokenNotExistsException;
import com.baibei.authserver.repository.RefreshTokenRepository;
import com.baibei.authserver.service.JwtService;
import com.baibei.authserver.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken generateToken(User user) {
        String token = jwtService.generateRefreshToken(user);
        return refreshTokenRepository.save(new RefreshToken(token));
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotExistsException(token));
    }

    @Override
    public void deleteByToken(String token) {
        refreshTokenRepository.delete(findByToken(token));
    }
}
