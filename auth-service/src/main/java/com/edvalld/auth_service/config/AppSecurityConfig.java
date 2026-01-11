package com.edvalld.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    public AppSecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrfConfigurer -> csrfConfigurer.disable())   // TODO - JWT, best practice?
                .authorizeHttpRequests( auth -> auth
                        // .requestMatchers() // TODO - check against specific HTTP METHOD
                        .anyRequest().permitAll() // MUST exist AFTER matchers, TODO - Is this true by DEFAULT?
                )

                // TODO - If you want (optional), insert configure logic here for CORS

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return httpSecurity.build();
    }


}
