package com.aquaLex.contract.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aquaLex.common.exception.EmailAlreadyExistsException;
import com.aquaLex.common.exception.InvalidCredentialsException;
import com.aquaLex.common.exception.UsernameAlreadyExistsException;
import com.aquaLex.contract.domain.RefreshToken;
import com.aquaLex.contract.domain.User;
import com.aquaLex.contract.domain.UserRole;
import com.aquaLex.contract.dto.AuthResponse;
import com.aquaLex.contract.dto.LoginRequest;
import com.aquaLex.contract.dto.RegisterRequest;
import com.aquaLex.contract.dto.UserResponse;
import com.aquaLex.contract.infrastructure.RefreshTokenRepository;
import com.aquaLex.contract.infrastructure.UserRepository;
import com.aquaLex.contract.security.JwtService;

import java.time.Duration;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder ;
    private final UserRepository userRepository;
    private final JwtService jwtService;
private final TokenBlacklistService tokenBlacklistService;
private final RefreshTokenService refreshTokenService;

    
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, TokenBlacklistService tokenBlacklistService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.refreshTokenService = refreshTokenService;
    }
    public UserResponse registerUser(RegisterRequest registerRequest) {
        // Implement user registration logic here
        if (userRepository.existsByUserName(registerRequest.username())) {
            throw new UsernameAlreadyExistsException();
        }
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new EmailAlreadyExistsException();
        }
        String passwordHash  = passwordEncoder.encode(registerRequest.password());
        User newUser = new User(
                registerRequest.email(),
                passwordHash,
                registerRequest.username(),
                registerRequest.fullName(), 
                UserRole.USER // Default role
        );
        User savedUser = userRepository.save(newUser);
        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getUserName(),
                savedUser.getFullName(),
                savedUser.getRole(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt()
        );
    }
    public AuthResponse loginUser(LoginRequest loginRequest)
    {
         User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(InvalidCredentialsException::new);
         if (!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
         }
         String accessToken = jwtService.generateAccessToken(user);
         String refreshToken = refreshTokenService.createRefreshToken(user);
         return new AuthResponse(
            accessToken,
            refreshToken,
            "Bearer",
            900 // Access token expiration time in seconds (15 minutes)   
         );
    }
public void logoutUser(String accessToken, String rawRefreshToken) {
    Duration remainingLifetime =
            jwtService.getRemainingLifetime(accessToken);

    if (!remainingLifetime.isZero()
            && !remainingLifetime.isNegative()) {
        tokenBlacklistService.blacklist(
                accessToken,
                remainingLifetime
        );
    }
    refreshTokenService.revokeRefreshToken(rawRefreshToken);
}
    // public RefreshToken refreshAccessToken(String rawRefreshToken)
    // {
    //     String tokenHash = refreshTokenService.hashRefreshToken(rawRefreshToken);
    //     RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
    //             .orElseThrow(() -> new InvalidCredentialsException("Refresh token not found"));

    //     if (refreshToken == null || refreshToken.getExpiresAt().isBefore(java.time.Instant.now())) {
    //         throw new InvalidCredentialsException("Refresh token is invalid or expired");
    //     }
    //     if (refreshToken.isRevoked()) {
    //         throw new InvalidCredentialsException("Refresh token has been revoked");
    //     }
    //     return refreshToken;
    // }
    public AuthResponse refreshAccessToken(String rawRefreshToken){
        RefreshToken oldRefreshToken = refreshTokenService.validateRefreshToken(rawRefreshToken);
        User user = oldRefreshToken.getUser();
        oldRefreshToken.revoke();
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = refreshTokenService.createRefreshToken(user);
        return new AuthResponse(
            newAccessToken,
            newRefreshToken,
            "Bearer",
            900 // Access token expiration time in seconds (15 minutes)
        );
    }
    public UserResponse getUserProfile(String accessToken)
    {
        String userEmail = jwtService.extractEmail(accessToken);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserResponse(
            user.getId(),        
            user.getEmail(),     
            user.getUserName(),  
            user.getFullName(),  
            user.getRole(),      
            user.getCreatedAt(), 
            user.getUpdatedAt()  
        );
    }
    public UserResponse getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getFullName(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}