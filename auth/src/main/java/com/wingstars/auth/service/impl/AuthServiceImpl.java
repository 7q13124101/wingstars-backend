package com.wingstars.auth.service.impl;

import com.wingstars.auth.dto.request.LoginRequest;
import com.wingstars.auth.dto.request.RegisterRequest;
import com.wingstars.auth.dto.response.AuthResponse;
import com.wingstars.auth.entity.RefreshToken;
import com.wingstars.auth.entity.Role;
import com.wingstars.auth.entity.User;
import com.wingstars.auth.repository.RefreshTokenRepository;
import com.wingstars.auth.repository.RoleRepository;
import com.wingstars.auth.repository.UserRepository;
import com.wingstars.auth.service.AuthService;
import com.wingstars.core.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private static final String DEFAULT_USER_ROLE_CODE = "USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final long refreshTokenExpirationMs;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            @Value("${security.jwt.refresh-token-expiration-ms:604800000}") long refreshTokenExpirationMs
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Role userRole = roleRepository.findByCode(DEFAULT_USER_ROLE_CODE)
                .orElseThrow(() -> new RuntimeException("Default role USER not found"));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .gender(request.getGender())
                .address(request.getAddress())
                .imageUrl(request.getImageUrl())
                .role(userRole)
                .deleted(false)
                .build();

        userRepository.save(user);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshTokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(refreshTokenValue)
                .expiryDate(LocalDateTime.now().plus(refreshTokenExpirationMs, ChronoUnit.MILLIS))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .tokenType("Bearer")
                .build();
    }
}
