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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PostMapping("/register/by")
    public ResponseEntity<UserDto> registerByRole(
            @RequestBody RegisterRequest registerRequest, @RequestParam String role) {
        role = role.toUpperCase();
        if (!roleService.existsByName(role)) {
            return ResponseEntity.status(404).build();
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(roleService.findByName(role));

        user = userService.save(user);

        return ResponseEntity.ok(userMapper.toDto(userService.save(user)));
    }

    @PostMapping("/role/new")
    public String newRole(@RequestParam String role) {
        return roleService.save(new Role(role)).getName();
    }

    @PostMapping("/role/delete")
    public String deleteRole(@RequestParam String role) {
        role = role.toUpperCase();
        Role roleEntity = roleService.findByName(role);
        if (roleEntity.getName().equals("ADMIN")) {
            return "Can't delete admin role";
        }
        roleService.delete(roleEntity);
        return "Role successfully deleted";
    }

    @PostMapping("/scope/new")
    public String newScope(@RequestParam String scope) {
        return scopeService.save(new Scope(scope)).getName();
    }

    @PostMapping("/scope/delete")
    public String deleteScope(@RequestParam String scope) {
        scope = scope.toUpperCase();
        Scope scopeEntity = scopeService.findByName(scope);
        if (scopeEntity.getName().equals("ADMIN")) {
            return "Can't delete admin scope";
        }
        return "Scope successfully deleted";
    }

    @GetMapping("/get/user/{username}")
    public UserDto getUser(@PathVariable String username) {
        return userMapper.toDto(userService.findByUsername(username));
    }

    @PostMapping("/edit/{username}/add/scope")
    public boolean addScope(@PathVariable String username, @RequestParam String scope) {
        User user = userService.findByUsername(username);
        boolean success = user.addScope(scopeService.findByName(scope));
        userService.save(user);
        return success;
    }

    @PostMapping("/edit/{username}/add/scope/list")
    public boolean addScopeList(@PathVariable String username, @RequestBody List<String> scopes) {
        User user = userService.findByUsername(username);
        boolean success = true;
        for (String scope : scopes) {
            success = user.addScope(scopeService.findByName(scope));
        }
        userService.save(user);
        return success;
    }

    @PostMapping("/edit/{username}/remove/scope")
    public boolean removeScope(@PathVariable String username, @RequestParam String scope) {
        User user = userService.findByUsername(username);
        boolean success = user.removeScope(scopeService.findByName(scope));
        userService.save(user);
        return success;
    }

    @PostMapping("/edit/{username}/remove/scope/list")
    public boolean removeScopeList(@PathVariable String username, @RequestBody List<String> scopes) {
        User user = userService.findByUsername(username);
        boolean success = true;
        for (String scope : scopes) {
            success = user.removeScope(scopeService.findByName(scope));
        }
        userService.save(user);
        return success;
    }

    @PostMapping("/edit/{username}/set/role")
    public boolean setRole(@PathVariable String username, @RequestParam String role) {
        User user = userService.findByUsername(username);
        user.setRole(roleService.findByName(role));
        userService.save(user);
        return true;
    }

    @PostMapping("/edit/{username}/delete")
    public void delete(@PathVariable String username) {
        User user = userService.findByUsername(username);
        userService.delete(user);
    }

    @PostMapping("/edit/{username}")
    public UserDto edit(@PathVariable String username, @RequestBody UserDto userDto) {
        User user = userService.findByUsername(username);

        String newUsername = userDto.getUsername();
        if (newUsername != null && !newUsername.isEmpty()) {
            user.setUsername(newUsername);
        }

        String newPassword = userDto.getPassword();
        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        user = userService.save(user);

        return userMapper.toDto(user);
    }
}
