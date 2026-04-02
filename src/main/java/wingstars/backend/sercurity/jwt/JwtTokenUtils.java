package wingstars.backend.sercurity.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import wingstars.backend.exception.ApplicationRuntimeException;
import wingstars.backend.sercurity.user.UserPrincipal;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);

    @Value("${wingstars.jwtExpiration}")
    private int jwtExpirationMs;

    @Value("${wingstars.jwtExpirationRefreshToken}")
    private long jwtExpirationRefreshToken;

    private final JwtParser jwtParser;
    private final JwtParser refreshJwtParser;

    @Getter
    private final Key key;
    private final Key refreshKey;

    public JwtTokenUtils(
            @Value("${wingstars.jwtSecret}") String jwtSecret,
            @Value("${wingstars.jwtRefreshTokenSecret}") String jwtRefreshTokenSecret) {

        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.refreshKey = Keys.hmacShaKeyFor(jwtRefreshTokenSecret.getBytes());

        this.jwtParser = Jwts.parser().verifyWith((javax.crypto.SecretKey) key).build();
        this.refreshJwtParser = Jwts.parser().verifyWith((javax.crypto.SecretKey) refreshKey).build();
    }

    public String generateJwtToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authentication.getAuthorities());

        return Jwts.builder()
                .claims(claims)
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public Claims getAllClaimsFromToken(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    public boolean validateJwtToken(String jwtToken) {
        try {
            jwtParser.parseSignedClaims(jwtToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            throw e;
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
            throw new ApplicationRuntimeException("Token is expired", HttpStatus.GONE);
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
            throw e;
        }
        return false;
    }

    public void validateRefreshJwtToken(String refreshToken) {
        try {
            refreshJwtParser.parseSignedClaims(refreshToken);
        } catch (Exception e) {
            logger.error("Refresh JWT validation error: {}", e.getMessage());
            throw e;
        }
    }

    public String generateJwtTokenRefresh() {
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationRefreshToken))
                .signWith(refreshKey)
                .compact();
    }
}