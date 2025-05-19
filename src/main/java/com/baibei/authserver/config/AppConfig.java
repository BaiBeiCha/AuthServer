package com.baibei.authserver.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private final List<String> standardScopes = List.of(
            "CHANGE_USERS",
            "CHANGE_ROLES",
            "CHANGE_SCOPES",
            "CREATE_ADMIN",
            "CREATE_BY"
    );

    private Set<String> roles;
    private Set<String> scopes;
    private Set<String> permittedRoles;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;
}