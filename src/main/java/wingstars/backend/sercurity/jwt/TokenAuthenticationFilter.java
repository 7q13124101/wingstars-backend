package wingstars.backend.sercurity.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import wingstars.backend.sercurity.user.UserContext;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isEmpty;


public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private static final WeakConcurrentHashMap<String, UserContext> cachedAuthen = new WeakConcurrentHashMap<>(3600000);
    public static Map<UUID, Authentication> cachedAuthenticationByRequest = new WeakConcurrentHashMap<>(60000);

    public TokenAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public static String toMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(input.getBytes());
            String uniqueHash = bytesToHex(hash);
            System.out.println("Unique hash string: " + uniqueHash);
            return uniqueHash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return input;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws IOException, ServletException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        if (!tokenProvider.validate(token)) {
            chain.doFilter(request, response);
            return;
        }
        String tokenHash = toMD5(token);

        if (cachedAuthen.containsKey(tokenHash)) {
            SecurityContextHolder.getContext().setAuthentication(cachedAuthen.get(tokenHash).getAuthentication());
            cachedAuthenticationIfExistRequestId(request, cachedAuthen.get(tokenHash).getAuthentication());
            chain.doFilter(request, response);
            System.out.println("Get cached authentication object");
        } else {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = tokenProvider.loginWithLocal(request, response, chain, request, response, token);
            if (!cachedAuthen.containsKey(tokenHash)) {
                // TODO: On refresh of Token (grant, add, remove), need to update the Tokens to ensure next request correctly perform
                cachedAuthen.put(tokenHash, new UserContext(usernamePasswordAuthenticationToken));
                System.out.println("Put cached authentication object");
            }
            cachedAuthenticationIfExistRequestId(request, cachedAuthen.get(tokenHash).getAuthentication());
            chain.doFilter(request, response);
        }
    }

    private void cachedAuthenticationIfExistRequestId(HttpServletRequest request, Authentication usernamePasswordAuthenticationToken) {
        var requestId = request.getHeader("request-id");
        if (Objects.nonNull(requestId) && isUUID(requestId)) {
            cachedAuthenticationByRequest.put(UUID.fromString(requestId), usernamePasswordAuthenticationToken);
        }
    }

    private boolean isUUID(String requestId) {
        try {
            UUID.fromString(requestId);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}

