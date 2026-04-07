package com.wingstars.auth.service;

import com.wingstars.auth.dto.request.LoginRequest;
import com.wingstars.auth.dto.request.RegisterRequest;
import com.wingstars.auth.dto.response.AuthResponse;

public interface AuthService {
    void register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
