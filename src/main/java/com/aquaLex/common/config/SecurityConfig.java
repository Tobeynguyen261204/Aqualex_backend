package com.aquaLex.common.config;

import jakarta.servlet.DispatcherType;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

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
                        .requestMatchers(HttpMethod.POST, "/api/v1/contracts").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/contracts/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/contracts").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
