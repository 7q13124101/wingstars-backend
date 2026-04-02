package wingstars.backend.sercurity.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import wingstars.backend.sercurity.auth_token.UserAuthenticationToken;

import java.io.IOException;
import java.util.stream.Collectors;


public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationFilter.class);
    private final TokenProvider tokenProvider;


    public CustomAuthenticationFilter(TokenProvider tokenProvider) {
        super("/");
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserAuthenticationToken authRequest;
        UsernamePasswordAuthenticationToken loginToken;
        try {
            authRequest = getAuthRequest(request);
        } catch (IOException | JSONException | NullPointerException e) {
            LOGGER.error(" /AuthRequest: invalid parameters | From: {}", e.getClass().getName());
            throw new BadCredentialsException("Invalid parameters!");
        }
        UserDetails userDetails = tokenProvider.loadUserByUsername(authRequest);
        boolean isMatches;
        try {
            isMatches = tokenProvider.isMatches(authRequest, userDetails);
        } catch (IllegalArgumentException iae) {
            throw new BadCredentialsException("Password is not valid/missing salt!");
        }

        if (isMatches) {
            authRequest.setUserName(userDetails.getUsername());
            if (ObjectUtils.isEmpty(userDetails.getAuthorities()))
                throw new InternalAuthenticationServiceException("Cant build roles for IU: " + userDetails.getUsername() + " | cause missing role/service error!");
            authRequest.setAuthorities(userDetails.getAuthorities());
            loginToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } else {
            response.setHeader("userName", null);
            throw new BadCredentialsException("Incorrect username or password!");
        }
        return loginToken;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        tokenProvider.generateAccessTokenAndWriteToResponse(response, authResult);
    }

    private UserAuthenticationToken getAuthRequest(HttpServletRequest request) throws IOException, JSONException {
        String obj = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject jsonObject = new JSONObject(obj);
        String userName = jsonObject.get("userName").toString().replace("\"", "");
        String password = jsonObject.get("password").toString().replace("\"", "");
        return new UserAuthenticationToken(userName, password);
    }


}
