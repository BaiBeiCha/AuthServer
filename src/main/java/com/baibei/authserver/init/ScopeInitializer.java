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
public class ScopeInitializer {

    private final RoleService roleService;
    private final AppConfig appConfig;


    @EventListener(ApplicationReadyEvent.class)
    public void initScopes() {
        appConfig.getScopes().addAll(appConfig.getStandardScopes());
        appConfig.getScopes().forEach(scopeName -> {
            if (!roleService.existsByName(scopeName)) {
                Role role = new Role();
                role.setName(scopeName);
                roleService.save(role);
            }
        });
    }
}
