package com.aquaLex.contract.security;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aquaLex.contract.application.TokenBlacklistService;
import com.aquaLex.contract.domain.User;
import com.aquaLex.contract.infrastructure.UserRepository;

import io.jsonwebtoken.JwtException;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
@Component 
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserRepository userRepository;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    public JwtAuthenticationFilter(JwtService jwtService, TokenBlacklistService tokenBlacklistService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.userRepository = userRepository;
    }
    @Override 
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
      String authHeader = request.getHeader("Authorization");
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
       String accessTokenToExamine = authHeader.substring(7);
       try{
        if(tokenBlacklistService.isBlacklisted(accessTokenToExamine)){
            filterChain.doFilter(request, response);
            return;
        }
        String userEmail = jwtService.extractEmail(accessTokenToExamine);
     
        User user = userRepository.findByEmail(userEmail).orElse(null); 
        if (user != null
            && jwtService.isTokenValid(accessTokenToExamine, user)
            && SecurityContextHolder.getContext().getAuthentication() == null) {

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_" + user.getRole().name()
                                )
                        )
                );
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }
       }catch(JwtException | IllegalArgumentException e){
        log.debug("Invalid JWT token: {}", e.getMessage());
        // Do nothing and proceed with the filter chain
    }
        filterChain.doFilter(request, response);
    }
}
