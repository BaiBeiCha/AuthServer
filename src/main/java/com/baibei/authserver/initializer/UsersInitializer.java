package com.baibei.authserver.initializer;

import com.baibei.authserver.config.AppConfig;
import com.baibei.authserver.entity.Scope;
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
public class UsersInitializer {

    private final UserService userService;
    private final RoleService roleService;
    private final ScopeService scopeService;
    private final PasswordEncoder passwordEncoder;
    private final AppConfig appConfig;

    @EventListener(ApplicationReadyEvent.class)
    public void initUsers() {
        for (AppConfig.UserConfig userConfig : appConfig.getUsers()) {
            if (!userService.existsByUsername(userConfig.getUsername())) {
                User user = new User();
                user.setUsername(userConfig.getUsername());
                user.setPassword(passwordEncoder.encode(userConfig.getPassword()));
                user.setRole(roleService.findByName(userConfig.getRole()));
                user.setScopes(userConfig.getScopes().stream().map(scopeService::findByName).toList());
                userService.save(user);
                log.info("Initialized user: {}", user.getUsername());
            }
        }
    }
}
