package wingstars.backend.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wingstars.backend.dtos.request.RegisterUserRequest;
import wingstars.backend.dtos.response.UserResponse;
import wingstars.backend.entities.Role;
import wingstars.backend.entities.User;
import wingstars.backend.exception.ApplicationRuntimeException;
import wingstars.backend.repository.UserRepository;
import wingstars.backend.services.UserService;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder bCrypt;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<?> registerUser(RegisterUserRequest request) {
        if (ObjectUtils.isNotEmpty(userRepository.findByUserName(request.getUserName()))) {
            throw new ApplicationRuntimeException("User already exists");
        }
        Role role;
        if (!StringUtils.isEmpty(request.getRole())) {
            role = Role.getByShortName(request.getRole());
            if (Objects.isNull(role)) {
                throw new ApplicationRuntimeException("Invalid role", HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new ApplicationRuntimeException("Invalid role", HttpStatus.BAD_REQUEST);
        }
        if (Objects.isNull(request.getUserName())) {
            throw new ApplicationRuntimeException("UserId invalid");
        }
        String hashPw = bCrypt.encode(request.getPassword());
        User user = userRepository.save(User.builder()
                .userName(request.getUserName())
                .password(hashPw)
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .fullName(request.getFullName())
                .gender(request.getGender())
                .imageUrl(request.getImageUrl())
                .role(role)
                .build());
        userRepository.save(user);
        return ResponseEntity.ok()
                .body(modelMapper.map(user, UserResponse.class));
    }

}
