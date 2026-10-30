package com.aquaLex.common.config;

import jakarta.servlet.DispatcherType;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aquaLex.common.security.CustomAccessDeniedHandler;
import com.aquaLex.common.security.CustomAuthenticationEntryPoint;
import com.aquaLex.contract.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final CustomAuthenticationEntryPoint authenticationEntryPoint;
        private final CustomAccessDeniedHandler accessDeniedHandler;

        public SecurityConfig(
                JwtAuthenticationFilter jwtAuthenticationFilter,
                CustomAuthenticationEntryPoint authenticationEntryPoint,
                CustomAccessDeniedHandler accessDeniedHandler
        ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
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
                "/api/v1/health",
                "/api/v1/auth/register",
                "/api/v1/auth/login",
                "/api/v1/auth/refresh"
        ).permitAll()
                           .requestMatchers(
                            "/api/v1/auth/profile",
                            "/api/v1/auth/logout",
                            "/api/v1/contracts",
                            "/api/v1/contracts/**"
                    ).authenticated()
                        .anyRequest().authenticated()
)
                           .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
                );
               
                

        return http.build();
    }
}
