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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.security.SecureRandom;
import java.util.Base64;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

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
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String plainRefreshToken = generateRefreshToken();
        String hashedRefreshToken = hashToken(plainRefreshToken);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(hashedRefreshToken)
                .expiryDate(LocalDateTime.now().plus(refreshTokenExpirationMs, ChronoUnit.MILLIS))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(plainRefreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        String hashedIncomingToken = hashToken(refreshToken);
        RefreshToken storedToken = refreshTokenRepository.findByToken(hashedIncomingToken)
                .orElseThrow(() -> new RuntimeException("Refresh token is invalid"));

        if (Boolean.TRUE.equals(storedToken.getRevoked())) {
            throw new RuntimeException("Refresh token has been revoked");
        }

        if (storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            storedToken.setRevoked(true);
            refreshTokenRepository.save(storedToken);
            throw new RuntimeException("Refresh token has expired");
        }
        
        // Rotate token
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        String accessToken = jwtTokenProvider.generateAccessToken(storedToken.getUser());
        String newPlainRefreshToken = generateRefreshToken();
        String newHashedRefreshToken = hashToken(newPlainRefreshToken);

        RefreshToken newRefreshToken = RefreshToken.builder()
                .user(storedToken.getUser())
                .token(newHashedRefreshToken)
                .expiryDate(LocalDateTime.now().plus(refreshTokenExpirationMs, ChronoUnit.MILLIS))
                .revoked(false)
                .build();
        refreshTokenRepository.save(newRefreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newPlainRefreshToken)
                .tokenType("Bearer")
                .build();
    }

    private String generateRefreshToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[64];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash token", e);
        }
    }
}
