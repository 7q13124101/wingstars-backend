package com.wingstars.auth.controller;

import com.wingstars.auth.dto.request.LoginRequest;
import com.wingstars.auth.dto.request.RefreshTokenRequest;
import com.wingstars.auth.dto.request.RegisterRequest;
import com.wingstars.auth.dto.response.AuthResponse;
import com.wingstars.auth.service.AuthService;
import com.wingstars.core.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "APIs for user registration, login, and token management")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account with default USER role.")
    public ResponseEntity<ApiResponse<Object>> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user and return access & refresh tokens.")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.login(request)));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh token", description = "Obtain a new access token using a valid refresh token.")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.refreshToken(request.getRefreshToken())));
    }
}
