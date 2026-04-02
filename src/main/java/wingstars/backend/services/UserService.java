package wingstars.backend.services;

import org.springframework.http.ResponseEntity;
import wingstars.backend.dtos.request.RegisterUserRequest;

public interface UserService {
    ResponseEntity<?> registerUser(RegisterUserRequest registerUserRequest);

}
