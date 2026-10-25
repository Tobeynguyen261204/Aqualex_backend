package com.aquaLex.common.config;

import jakarta.servlet.DispatcherType;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(
                                DispatcherType.FORWARD,
                                DispatcherType.ERROR
                        ).permitAll()
                        .requestMatchers(
                                "/api/docs",
                                "/api/docs/**",
                                "/api-docs",
                                "/api-docs/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/api/swagger-ui/**",
                                "/swagger-ui/**",
                                "/webjars/**"
                        ).permitAll()
                       .requestMatchers(
                "/api/v1/contracts",
                "/api/v1/contracts/**",
                "/api/v1/auth/**",
                "/api/v1/auth"
                
        ).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
