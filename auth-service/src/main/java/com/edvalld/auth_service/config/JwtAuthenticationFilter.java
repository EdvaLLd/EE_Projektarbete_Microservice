package com.edvalld.auth_service.config;

import com.edvalld.JwtUtils;
import com.edvalld.auth_service.user.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    private final String keyValue;

    @Autowired
    public JwtAuthenticationFilter(CustomUserDetailsService customUserDetailsService, @Value("${base64.secret.key}")String base64EncodedSecretKey) {
        this.jwtUtils = JwtUtils.getInstance();
        this.customUserDetailsService = customUserDetailsService;
        keyValue = base64EncodedSecretKey;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String token = extractJwtFromCookie(request);
        if (token == null) {
            token = extractJwtFromRequest(request); // fallback to Authorization header
        }

        if (token != null && jwtUtils.validateJwtToken(token, keyValue)) {
            String username = jwtUtils.getUsernameFromJwtToken(token, keyValue);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // DB lookup i AuthService
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                if (userDetails != null && userDetails.isEnabled()) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("Authenticated (DB verified) user '{}'", username);
                } else {
                    logger.warn("User '{}' not found or disabled", username);
                }
            }
        } else if (token != null) {
            logger.warn("Invalid JWT token");
        }

        filterChain.doFilter(request, response);
    }

    // --- Hjälpmetoder ---

    private String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("authToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
