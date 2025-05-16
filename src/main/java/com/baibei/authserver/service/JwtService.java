package com.baibei.authserver.service;

import com.baibei.authserver.entity.User;
import io.jsonwebtoken.Claims;

import java.util.function.Function;

public interface JwtService {
    String generateToken(User user);

    String generateRefreshToken(User user);

    String extractUsername(String token);

    boolean validateToken(String token, User user);

    Boolean isTokenExpired(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    Claims extractAllClaims(String token);

    String extractAllInfo(String token);
}
