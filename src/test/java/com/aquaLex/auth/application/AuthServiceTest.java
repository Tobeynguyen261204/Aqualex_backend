package com.aquaLex.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.aquaLex.auth.domain.User;
import com.aquaLex.auth.domain.UserRole;
import com.aquaLex.auth.dto.AuthResponse;
import com.aquaLex.auth.dto.LoginRequest;
import com.aquaLex.auth.infrastructure.UserRepository;
import com.aquaLex.auth.security.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(
                userRepository,
                passwordEncoder,
                jwtService,
                tokenBlacklistService,
                refreshTokenService
        );
    }

    @Test
    void shouldReturnTokenWhenCredentialsAreValid() {
        User user = new User(
                "khoa@gmail.com",
                "hashed-password",
                "khoa",
                "Khoa Nguyen",
                UserRole.USER
        );
        LoginRequest loginRequest = new LoginRequest(
                "khoa@gmail.com",
                "123456"
        );

        when(userRepository.findByEmail(loginRequest.email()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(
                loginRequest.password(),
                user.getPasswordHash()
        )).thenReturn(true);
        when(jwtService.generateAccessToken(user))
                .thenReturn("access-token");
        when(refreshTokenService.createRefreshToken(user))
                .thenReturn("refresh-token");

        AuthResponse authResponse = authService.loginUser(loginRequest);

        assertThat(authResponse.accessToken()).isEqualTo("access-token");
        assertThat(authResponse.refreshToken()).isEqualTo("refresh-token");
        assertThat(authResponse.tokenType()).isEqualTo("Bearer");
        assertThat(authResponse.expiresIn()).isEqualTo(900);

        verify(userRepository).findByEmail("khoa@gmail.com");
        verify(passwordEncoder).matches("123456", "hashed-password");
        verify(jwtService).generateAccessToken(user);
        verify(refreshTokenService).createRefreshToken(user);
    }
}
