package com.baibei.authserver.initializer;

import com.baibei.authserver.config.AppConfig;
import com.baibei.authserver.entity.Scope;
import com.baibei.authserver.service.ScopeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScopeInitializer {

    private final ScopeService scopeService;
    private final AppConfig appConfig;

    @Getter
    private boolean initialized = false;

    @EventListener(ApplicationReadyEvent.class)
    public void initScopes() {
        StringBuilder scopesString = new StringBuilder();
        AtomicBoolean init = new AtomicBoolean(false);
        appConfig.getScopes().addAll(appConfig.getStandardScopes());
        appConfig.getScopes().forEach(scopeName -> {
            if (!scopeService.existsByName(scopeName)) {
                Scope scope = new Scope();
                scope.setName(scopeName);
                scopeService.save(scope);
                scopesString.append(scopeName).append(",");
                init.set(true);
            }
        });
        if (init.get()) {
            log.info("Initialized scopes: {}", scopesString.deleteCharAt(scopesString.length() - 1));
        }
        initialized = init.get();
    }
}
