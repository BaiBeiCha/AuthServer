package com.baibei.authserver.config;

import com.baibei.authserver.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static com.baibei.authserver.entity.Role.role;
import static com.baibei.authserver.entity.Scope.scope;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/admin/register",
                                "/api/auth/admin/register/**"
                        )
                        .hasAnyAuthority(
                                role("ADMIN"),
                                scope("CREATE_ADMIN")
                        )

                        .requestMatchers("/api/auth/admin/**")
                        .hasAnyAuthority(
                                role("ADMIN"),
                                scope("CHANGE_USERS")
                        )

                        .requestMatchers(
                                "/api/auth/admin/get/**",
                                "/api/auth/admin/edit/**"
                        ).hasAnyAuthority(
                                role("ADMIN"),
                                scope("CHANGE_USERS")
                        )

                        .requestMatchers(
                                "/api/auth/admin/role/**"
                        ).hasAnyAuthority(
                                role("ADMIN"),
                                scope("CHANGE_ROLES")
                        )

                        .requestMatchers(
                                "/api/auth/admin/scope/**"
                        ).hasAnyAuthority(
                                role("ADMIN"),
                                scope("CHANGE_SCOPES")
                        )

                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login", "/register", "/css/**", "/js/**", "/images/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/profile", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .sessionManagement(session -> session.
                        sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    private UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
