package com.baibei.authserver.init;

import com.baibei.authserver.config.AppConfig;
import com.baibei.authserver.entity.Role;
import com.baibei.authserver.entity.User;
import com.baibei.authserver.service.RoleService;
import com.baibei.authserver.service.ScopeService;
import com.baibei.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserService userService;
    private final RoleService roleService;
    private final ScopeService scopeService;
    private final PasswordEncoder passwordEncoder;
    private final AppConfig appConfig;

    @EventListener(ApplicationReadyEvent.class)
    public void initRoles() {
        if (!roleService.existsByName("ADMIN")) {
            roleService.save(new Role("ADMIN"));
        }

        if (!userService.existsByUsername("admin")) {
            User user = new User();
            user.setUsername(appConfig.getAdminUsername());
            user.setPassword(passwordEncoder.encode(appConfig.getAdminPassword()));
            user.setRole(roleService.findByName("ADMIN"));
            user.setScopes(scopeService.findAll());
            userService.save(user);
        }
    }
}
