package com.baibei.authserver.filter;

import com.baibei.authserver.entity.User;
import com.baibei.authserver.service.JwtService;
import com.baibei.authserver.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        log.debug("Authorization Header: {}", authHeader);
        final String jwt;
        final String username;

        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            log.debug("Invalid/missing Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        log.debug("JWT: {}", jwt);
        username = jwtService.extractUsername(jwt);
        log.debug("Extracted username: {}", username);

        if (!StringUtils.isEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.findByUsername(username);
            if (user == null) {
                filterChain.doFilter(request, response);
                return;
            }

            log.debug("Validating token: {}", jwtService.extractAllInfo(jwt));
            if (jwtService.validateToken(jwt, user)) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                );
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
            log.debug("Token validation result: {}", jwtService.validateToken(jwt, user));
        }

        filterChain.doFilter(request, response);
    }
}