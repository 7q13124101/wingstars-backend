package wingstars.backend.sercurity.jwt;

import lombok.Data;

import java.util.Collection;

@Data
public class JwtResponse {
    private String username;
    private String displayName;
    private Long expiration;
    private String token;
    private String refreshToken;
    private Collection<?> role;
    private String photoURL;

    // todo
    public JwtResponse(String username,
                       String displayName,
                       Long expiration,
                       String token,
                       String refreshToken,
                       String photoUrl,
                       Collection<?> role) {
        this.username = username;
        this.displayName = displayName;
        this.expiration = expiration;
        this.token = token;
        this.refreshToken = refreshToken;
        this.photoURL = photoUrl;
        this.role = role;
    }
}
