package com.aquaLex.contract.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aquaLex.common.response.ApiResponse;
import com.aquaLex.contract.application.AuthService;
import com.aquaLex.contract.dto.AuthResponse;
import com.aquaLex.contract.dto.LoginRequest;
import com.aquaLex.contract.dto.RegisterRequest;
import com.aquaLex.contract.dto.UserResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.aquaLex.common.exception.InvalidTokenException;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
@PostMapping("/register")
@Operation (summary = "Register a new user", description = "Registers a new user with the provided details.")
@ResponseStatus(HttpStatus.CREATED)
public ApiResponse<UserResponse> registerUser(@Valid @RequestBody RegisterRequest  registerRequest, HttpServletRequest request)  
{
    UserResponse userResponse = authService.registerUser(registerRequest);
    return ApiResponse.success(
        "USER_REGISTERED",
        "User registered successfully",
        userResponse,
        request.getRequestURI()
    );
  
}
@PostMapping("/login")
@Operation (summary = "Login a user", description = "Authenticates a user with the provided credentials.")
@ResponseStatus(HttpStatus.OK)
public ApiResponse<AuthResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest,   HttpServletRequest request)  
{
      AuthResponse authResponse = authService.loginUser(loginRequest);
      return ApiResponse.success(
        "USER_LOGGED_IN",
        "User logged in successfully",
        authResponse,
        request.getRequestURI()
    );
}
@PostMapping("/logout")
@Operation (summary = "Logout a user", description = "Logs out the currently authenticated user.")
@SecurityRequirement(name = "bearerAuth")
@ResponseStatus(HttpStatus.OK)
@SecurityRequirement(name = "bearerAuth")
public ApiResponse<Void> logoutUser(
        @Parameter(hidden = true)
        @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
        String authorizationHeader,
        @Parameter(description = "Raw refresh token returned by login", example = "refresh-token-value")
        @RequestHeader(value = "Refresh-Token", required = false)
        String refreshToken,
        HttpServletRequest request) {

    if (authorizationHeader == null
            || !authorizationHeader.startsWith("Bearer ")) {
        throw new InvalidTokenException(
                "Authorization header is missing or invalid"
        );
    }

    String accessToken = authorizationHeader.substring(7);
    if (refreshToken == null || refreshToken.isBlank()) {
        throw new InvalidTokenException(
                "Refresh-Token header is missing or empty"
        );
    }

    authService.logoutUser(accessToken, refreshToken);

    return ApiResponse.success(
            "USER_LOGGED_OUT",
            "User logged out successfully",
            null,
            request.getRequestURI()
    );

}
@PostMapping("/refresh")
@Operation (summary = "Refresh access token", description = "Refreshes the access token using a valid refresh token")
@SecurityRequirement(name = "bearerAuth")
public ApiResponse<AuthResponse> refreshToken(
        @Parameter(description = "Raw refresh token returned by login", example = "refresh-token-value")
        @RequestHeader(value = "Refresh-Token", required = false)
        String refreshToken,
        HttpServletRequest request) {

    if (refreshToken == null || refreshToken.isBlank()) {
        throw new InvalidTokenException(
                "Refresh-Token header is missing or empty"
        );
    }

    AuthResponse authResponse = authService.refreshAccessToken(refreshToken);

    return ApiResponse.success(
            "ACCESS_TOKEN_REFRESHED",
            "Access token refreshed successfully",
            authResponse,
            request.getRequestURI()
    );

}
@GetMapping ("/profile")
@Operation (summary = "Get user profile", description = "Retrieves the profile information of the currently authenticated user.")
@SecurityRequirement(name = "bearerAuth")
public ApiResponse<UserResponse> getUserProfile(
        @Parameter(hidden = true)
        @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
        Authentication authentication,
        HttpServletRequest request) {
    String email = authentication.getName();
    UserResponse userResponse = authService.getUserProfileByEmail(email);

    return ApiResponse.success(
            "USER_PROFILE_RETRIEVED",
            "User profile retrieved successfully",
            userResponse,
            request.getRequestURI()
    );
}
}