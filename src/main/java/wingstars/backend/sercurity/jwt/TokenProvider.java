package wingstars.backend.sercurity.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import wingstars.backend.entities.RefreshToken;
import wingstars.backend.entities.User;
import wingstars.backend.repository.RefreshTokenRepository;
import wingstars.backend.repository.UserRepository;
import wingstars.backend.sercurity.auth_token.UserAuthenticationToken;
import wingstars.backend.sercurity.user.CustomUserDetailsService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);
    @Value("${wingstars.jwtExpirationRefreshToken}")
    private long jwtExpirationRefreshToken;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;

    public UserDetails loadUserByUsername(UserAuthenticationToken authRequest) {
        return customUserDetailsService.loadUserByUsername(authRequest.getUserName());
    }

    public boolean isMatches(UserAuthenticationToken authRequest, UserDetails userDetails) {
        return passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword());
    }

    public void generateAccessTokenAndWriteToResponse(HttpServletResponse response, Authentication authResult) throws IOException {
        String token = jwtUtils.generateJwtToken(authResult);
        String refreshToken = jwtUtils.generateJwtTokenRefresh();
        Claims allClaims = jwtUtils.getAllClaimsFromToken(token);
        Collection<?> roles = authResult.getAuthorities();
        String userName = allClaims.getSubject();
        User user = userRepository.findByUserName(userName);
        createRefreshToken(user, refreshToken);
        Long expiration = allClaims.getExpiration().getTime();
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(new JwtResponse(user.getUserName(), user.getFullName(), expiration,
                token,refreshToken, user.getUserName() ,roles)));
        LOGGER.info("User login success oki {} ", user.getId());
    }

    private void createRefreshToken(User user, String refreshToken) {
        List<RefreshToken> refreshToken1 = refreshTokenRepository.findAllByUser(user);
        refreshTokenRepository.deleteAll(refreshToken1);
        refreshTokenRepository.save(RefreshToken.builder().refreshTokenCode(refreshToken).user(user).expiryDate(Instant.now().plusMillis(jwtExpirationRefreshToken)).build());
    }


    public boolean validate(String jwt) {
        return jwtUtils.validateJwtToken(jwt);
    }

    public UsernamePasswordAuthenticationToken loginWithLocal(ServletRequest servletRequest, ServletResponse servletResponse,
                                                              FilterChain filterChain, HttpServletRequest request,
                                                              HttpServletResponse response, String jwt) throws IOException, ServletException {
        LOGGER.debug(" * validating jwt...");
        try {
            jwtUtils.validateJwtToken(jwt);
        } catch (ExpiredJwtException e) {
            response.sendError(HttpServletResponse.SC_GONE, e.getMessage());
            return null;
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return null;
        }
        Claims claims = jwtUtils.getAllClaimsFromToken(jwt);
        String userName = claims.getSubject();
        LOGGER.debug(" * Checking user's token: " + userName);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Consumer<Map> consumer = (Map item) -> {
            authorities.add(new SimpleGrantedAuthority(item.get("role").toString()));
        };
        claims.get("authorities", List.class).stream().forEach(consumer);
        User userDetails = userRepository.findByUserName(userName);
        return createSecurityContexHolder(userDetails, authorities, request);
    }

    private UsernamePasswordAuthenticationToken createSecurityContexHolder(User userDetails, List<SimpleGrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        if (authentication.getAuthorities().isEmpty()) {
            throw new PreAuthenticatedCredentialsNotFoundException("Not found authenticated or anonymous user!");
        }
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
