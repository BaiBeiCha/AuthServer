package com.baibei.authserver.controller;

import com.baibei.authserver.dto.*;
import com.baibei.authserver.entity.User;
import com.baibei.authserver.mapper.UserMapper;
import com.baibei.authserver.service.JwtService;
import com.baibei.authserver.service.RefreshTokenService;
import com.baibei.authserver.service.RoleService;
import com.baibei.authserver.service.UserService;
import com.baibei.authserver.service.kafka.producer.UserRegistrationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public UserDto register(@RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(roleService.findByName("USER"));

        user = userService.save(user);

        return userMapper.toDto(user);
    }

    @PostMapping("/register/by")
    public ResponseEntity<UserDto> registerByRole(
            @RequestBody RegisterRequest registerRequest, @RequestParam String role) {
        role = role.toUpperCase();
        if (!roleService.existsByName(role) || role.contains("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(roleService.findByName(role));

        user = userService.save(user);

        return ResponseEntity.ok(userMapper.toDto(userService.save(user)));
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userService.findByUsername(request.getUsername());

        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.generateToken(user).getToken();

        return new AuthResponse(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshTokenRequest request, @RequestParam Boolean refreshBoth) {
        String refreshToken = request.getRefreshToken();
        String username = jwtService.extractUsername(refreshToken);

        User user = userService.findByUsername(username);

        if (jwtService.validateToken(refreshToken, user)) {
            String accessToken = jwtService.generateToken(user);
            if (refreshBoth != null && refreshBoth) {
                refreshToken = refreshTokenService.generateToken(user).getToken();
            }
            return new AuthResponse(accessToken, refreshToken);
        } else {
            throw new IllegalArgumentException("Invalid refresh token");
        }
    }

    @PostMapping("/logout")
    public String logout(@RequestBody RefreshTokenRequest request) {
        refreshTokenService.deleteByToken(request.getRefreshToken());
        return "Logout successful";
    }

    @PostMapping("/expired")
    public boolean expired(@RequestBody TokenIsExpiredRequest request) {
        return jwtService.isTokenExpired(request.getToken());
    }
}
