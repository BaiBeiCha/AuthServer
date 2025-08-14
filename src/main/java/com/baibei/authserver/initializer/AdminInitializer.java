package com.baibei.authserver.initializer;

import com.baibei.authserver.config.AppConfig;
import com.baibei.authserver.entity.Role;
import com.baibei.authserver.entity.User;
import com.baibei.authserver.service.RoleService;
import com.baibei.authserver.service.ScopeService;
import com.baibei.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@DependsOn({"scopeInitializer", "roleInitializer"})
public class AdminInitializer {

    private final UserService userService;
    private final RoleService roleService;
    private final ScopeService scopeService;
    private final PasswordEncoder passwordEncoder;
    private final AppConfig appConfig;
    private final ScopeInitializer scopeInitializer;
    private final RoleInitializer roleInitializer;

    @EventListener(ApplicationReadyEvent.class)
    public void initRoles() {
        if (!scopeInitializer.isInitialized()) {
            scopeInitializer.initScopes();
        }
        if (!roleInitializer.isInitialized()) {
            roleInitializer.initRoles();
        }

        if (!roleService.existsByName("ADMIN")) {
            roleService.save(new Role("ADMIN"));
            log.info("Initialized role: ADMIN");
        }

        if (!userService.existsByUsername(appConfig.getAdminUsername())) {
            User user = new User();
            user.setUsername(appConfig.getAdminUsername());
            user.setPassword(passwordEncoder.encode(appConfig.getAdminPassword()));
            user.setRole(roleService.findByName("ADMIN"));
            user.setScopes(scopeService.findAll());
            userService.save(user);
            log.info("Initialized user: {}", user.getUsername());
        }
    }
}
