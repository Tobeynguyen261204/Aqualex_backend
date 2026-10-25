package com.aquaLex.contract.controller;

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
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.aquaLex.common.exception.InvalidTokenException;

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
@ResponseStatus(HttpStatus.OK)
public ApiResponse<Void> logoutUser(HttpServletRequest request) {
       String authorizationHeader =
            request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authorizationHeader == null
            || !authorizationHeader.startsWith("Bearer ")) {
        throw new InvalidTokenException(
                "Authorization header is missing or invalid"
        );
    }

    String accessToken = authorizationHeader.substring(7);

    String refreshToken = request.getHeader("Refresh-Token");

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
}