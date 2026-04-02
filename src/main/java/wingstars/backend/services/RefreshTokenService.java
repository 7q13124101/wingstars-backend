package wingstars.backend.services;


import wingstars.backend.dtos.request.RefreshTokenRequest;

import java.util.Map;

public interface RefreshTokenService {
    void logout(String username);

    Map<String, String> refreshToken(String username, RefreshTokenRequest refreshTokenRequest);

    void clearRefreshToken();
}
