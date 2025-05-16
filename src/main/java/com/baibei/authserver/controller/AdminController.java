package com.baibei.authserver.controller;

import com.baibei.authserver.dto.RegisterRequest;
import com.baibei.authserver.dto.UserDto;
import com.baibei.authserver.entity.Role;
import com.baibei.authserver.entity.User;
import com.baibei.authserver.entity.Scope;
import com.baibei.authserver.mapper.UserMapper;
import com.baibei.authserver.service.RoleService;
import com.baibei.authserver.service.ScopeService;
import com.baibei.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final ScopeService scopeService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public UserDto adminRegister(@RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(roleService.findByName("ADMIN"));

        user = userService.save(user);

        return userMapper.toDto(user);
    }

    @PostMapping("/role/new")
    public String newRole(@RequestParam String role) {
        return roleService.save(new Role(role)).getName();
    }

    @PostMapping("/scope/new")
    public String newScope(@RequestParam String scope) {
        return scopeService.save(new Scope(scope)).getName();
    }

    @GetMapping("/exists/{username}")
    public boolean existsUser(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @GetMapping("/get/user/{username}")
    public UserDto getUser(@PathVariable String username) {
        return userMapper.toDto(userService.findByUsername(username));
    }
}
