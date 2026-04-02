package wingstars.backend.sercurity.auth_token;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class UserAuthenticationToken {
    private String userName;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserAuthenticationToken(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
