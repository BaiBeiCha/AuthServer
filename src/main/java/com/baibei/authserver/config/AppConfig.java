package com.baibei.authserver.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Data
@Slf4j
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

    private List<UserConfig> users;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Getter
    @Value("${app.locale}")
    private Locale locale;

    @Bean
    protected Locale locale() {
        log.info("Application locale: {}", locale);
        return locale;
    }

    public enum Locale {
        EN, RU
    }

    @Getter
    @Setter
    public static class UserConfig {
        private String username;
        private String password;
        private String role;
        private Set<String> scopes;
    }
}