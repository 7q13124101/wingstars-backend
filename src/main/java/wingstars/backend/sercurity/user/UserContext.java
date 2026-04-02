package wingstars.backend.sercurity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;

@Data
@AllArgsConstructor
public class UserContext {
    private Authentication authentication;
}
