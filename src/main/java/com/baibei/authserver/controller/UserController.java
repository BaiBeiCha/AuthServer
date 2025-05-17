package com.baibei.authserver.controller;

import com.baibei.authserver.dto.UserDto;
import com.baibei.authserver.entity.User;
import com.baibei.authserver.mapper.UserMapper;
import com.baibei.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @PostMapping("/edit/{username}")
    public ResponseEntity<UserDto> edit(@PathVariable String username, @RequestBody UserDto userDto,
                                        Authentication authentication) {
        User user = userService.findByUsername(username);
        if (!authentication.getName().equals(user.getUsername())) {
            return ResponseEntity.status(403).build();
        }

        String newUsername = userDto.getUsername();
        if (newUsername != null && !newUsername.isEmpty()) {
            user.setUsername(newUsername);
        }

        String newPassword = userDto.getPassword();
        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        user = userService.save(user);

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping("/edit/{username}/delete")
    public ResponseEntity<?> delete(@PathVariable String username, Authentication authentication) {
        User user = userService.findByUsername(username);
        if (!authentication.getName().equals(user.getUsername())) {
            return ResponseEntity.status(403).build();
        }

        userService.delete(user);
        return ResponseEntity.ok().build();
    }
}
