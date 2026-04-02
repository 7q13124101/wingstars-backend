package wingstars.backend.services.impl;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import wingstars.backend.dtos.request.RefreshTokenRequest;
import wingstars.backend.entities.RefreshToken;
import wingstars.backend.entities.User;
import wingstars.backend.exception.ApplicationRuntimeException;
import wingstars.backend.repository.RefreshTokenRepository;
import wingstars.backend.repository.UserRepository;
import wingstars.backend.sercurity.jwt.JwtTokenUtils;
import wingstars.backend.services.RefreshTokenService;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${wingstars.jwtExpiration}")
    private int jwtExpirationMs;
    @Value("${wingstars.jwtExpirationRefreshToken}")
    private long jwtExpirationRefreshToken;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;

    private final String MESSAGE_END_LOGIN_SESSION = "Login session expired, please contact admin";


    @Override
    public void logout(String username) {
        User user = userRepository.findByUserName(username);
        List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByUser(user);
        refreshTokenRepository.deleteAll(refreshTokens);
    }

    @Override
    public Map<String, String> refreshToken(String username, RefreshTokenRequest refreshTokenRequest) {
        try {
            jwtTokenUtils.validateRefreshJwtToken(refreshTokenRequest.getRefreshToken());
            RefreshToken refreshToken = refreshTokenRepository.findByRefreshTokenCode(refreshTokenRequest.getRefreshToken());
            if (ObjectUtils.isEmpty(refreshToken)) {
                throw new ApplicationRuntimeException(MESSAGE_END_LOGIN_SESSION, HttpStatus.UNAUTHORIZED);
            }
            User user = userRepository.findByUserName(username);
            Map<String, Object> claims = new HashMap<>();
            List<GrantedAuthority> authorities = Collections.
                    singletonList(new SimpleGrantedAuthority(user.getRole().name()));
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .addClaims(Map.of("authorities", authorities))
                    .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(jwtTokenUtils.getKey()).compact();
            String refreshTokenNew = jwtTokenUtils.generateJwtTokenRefresh();
            refreshToken.setRefreshTokenCode(refreshTokenNew);
            refreshToken.setExpiryDate(Instant.now().plusMillis(jwtExpirationRefreshToken));
            refreshTokenRepository.save(refreshToken);
            return Map.of("token", token, "refreshToken", refreshTokenNew);
        } catch (Exception e) {
            throw new ApplicationRuntimeException(MESSAGE_END_LOGIN_SESSION, HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public void clearRefreshToken() {
        refreshTokenRepository.deleteAll();
    }
}
