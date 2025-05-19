package com.baibei.authserver.init;

import com.baibei.authserver.config.AppConfig;
import com.baibei.authserver.entity.Role;
import com.baibei.authserver.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleService roleService;
    private final AppConfig appConfig;

    @EventListener(ApplicationReadyEvent.class)
    public void initRoles() {
        StringBuilder rolesString = new StringBuilder();
        AtomicBoolean init = new AtomicBoolean(false);
        appConfig.getRoles().forEach(roleName -> {
            if (!roleService.existsByName(roleName)) {
                Role role = new Role();
                role.setName(roleName);
                roleService.save(role);
                rolesString.append(role.getName()).append(",");
                init.set(true);
            }
        });
        if (init.get()) {
            log.info("Initialized roles: {}", rolesString.deleteCharAt(rolesString.length() - 1));
        }
    }
}
