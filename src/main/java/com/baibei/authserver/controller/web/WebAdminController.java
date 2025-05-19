package com.baibei.authserver.controller.web;

import com.baibei.authserver.dto.UserDto;
import com.baibei.authserver.entity.Role;
import com.baibei.authserver.entity.Scope;
import com.baibei.authserver.entity.User;
import com.baibei.authserver.mapper.UserMapper;
import com.baibei.authserver.service.RoleService;
import com.baibei.authserver.service.ScopeService;
import com.baibei.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class WebAdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final ScopeService scopeService;
    private final UserMapper userMapper;

    @GetMapping
    public String userList(Model model) {
        List<User> allUsers = userService.findAll();
        model.addAttribute("allUsers", allUsers);
        return "users";
    }

    @GetMapping("/edit/{username}")
    public String editUserPage(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/admin?error=UserNotFound";
        }
        UserDto userDto = userMapper.toDto(user);

        List<String> allRoles = roleService.findAll().stream().map(Role::getName).toList();
        List<String> allScopes = scopeService.findAll().stream().map(Scope::getName).toList();

        Set<String> userScopes = user.getScopes()
                .stream()
                .map(Scope::getName)
                .collect(Collectors.toSet());

        model.addAttribute("userDto", userDto);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("allScopes", allScopes);
        model.addAttribute("userScopes", userScopes);

        return "edit";
    }

    @PostMapping("/edit/{username}")
    public String updateUser(@PathVariable String username,
                             @ModelAttribute UserDto userDto) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/admin?error=UserNotFound";
        }
        if (userDto.getUsername() != null && !userDto.getUsername().isEmpty()) {
            user.setUsername(userDto.getUsername());
        }
        if (userDto.getRole() != null) {
            Role role = roleService.findByName(userDto.getRole());
            if (role != null) {
                user.setRole(role);
            }
        }
        userService.save(user);
        return "redirect:/admin/edit/" + user.getUsername() + "?success=UserUpdated";
    }

    @PostMapping("/edit/{username}/addScopes")
    public String addScopes(@PathVariable String username,
                            @RequestParam List<String> scopes) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/admin?error=UserNotFound";
        }
        for (String scopeName : scopes) {
            Scope scope = scopeService.findByName(scopeName);
            if (scope != null) {
                user.addScope(scope);
            }
        }
        userService.save(user);
        return "redirect:/admin/edit/" + username + "?success=ScopesAdded";
    }

    @PostMapping("/edit/{username}/removeScopes")
    public String removeScopes(@PathVariable String username,
                               @RequestParam List<String> scopes) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/admin?error=UserNotFound";
        }
        for (String scopeName : scopes) {
            Scope scope = scopeService.findByName(scopeName);
            if (scope != null) {
                user.removeScope(scope);
            }
        }
        userService.save(user);
        return "redirect:/admin/edit/" + username + "?success=ScopesRemoved";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            userService.delete(user);
        }
        return "redirect:/admin?success=UserDeleted";
    }

    @GetMapping("/roles")
    public String rolesPage(Model model) {
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        return "roles";
    }

    @PostMapping("/roles/new")
    public String createRole(@RequestParam String role) {
        if (role != null && !role.isBlank()) {
            roleService.save(new Role(role.toUpperCase()));
        }
        return "redirect:/admin/roles?success=RoleCreated";
    }

    @PostMapping("/roles/delete")
    public String deleteRole(@RequestParam String role) {
        Role roleEntity = roleService.findByName(role.toUpperCase());
        if (roleEntity != null && !"ADMIN".equals(roleEntity.getName())) {
            roleService.delete(roleEntity);
        }
        return "redirect:/admin/roles?success=RoleDeleted";
    }

    @GetMapping("/scopes")
    public String scopesPage(Model model) {
        List<Scope> scopes = scopeService.findAll();
        model.addAttribute("scopes", scopes);
        return "scopes";
    }

    @PostMapping("/scopes/new")
    public String createScope(@RequestParam String scope) {
        if (scope != null && !scope.isBlank()) {
            scopeService.save(new Scope(scope.toUpperCase()));
        }
        return "redirect:/admin/scopes?success=ScopeCreated";
    }

    @PostMapping("/scopes/delete")
    public String deleteScope(@RequestParam String scope) {
        Scope scopeEntity = scopeService.findByName(scope.toUpperCase());
        if (scopeEntity != null && !"ADMIN".equals(scopeEntity.getName())) {
            scopeService.delete(scopeEntity);
        }
        return "redirect:/admin/scopes?success=ScopeDeleted";
    }
}
