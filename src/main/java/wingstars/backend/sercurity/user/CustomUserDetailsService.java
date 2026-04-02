package wingstars.backend.sercurity.user;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import wingstars.backend.entities.User;
import wingstars.backend.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName)
            throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("User not found : " + userName);

        }
        return UserPrincipal.create(user);
    }
}