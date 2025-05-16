package com.baibei.authserver.init;

import com.baibei.authserver.config.AppConfig;
import com.baibei.authserver.entity.Role;
import com.baibei.authserver.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleService roleService;
    private final AppConfig appConfig;

    @EventListener(ApplicationReadyEvent.class)
    public void initRoles() {
        appConfig.getRoles().forEach(roleName -> {
            if (!roleService.existsByName(roleName)) {
                Role role = new Role();
                role.setName(roleName);
                roleService.save(role);
            }
        });
    }
}
