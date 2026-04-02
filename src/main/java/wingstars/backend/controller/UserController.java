package wingstars.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wingstars.backend.dtos.request.RefreshTokenRequest;
import wingstars.backend.dtos.request.RegisterUserRequest;
import wingstars.backend.services.RefreshTokenService;
import wingstars.backend.services.UserService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class UserController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    @PostMapping("/register")
    @Operation(summary = "User register", description = "Allows 2 roles: admin and user")
    public ResponseEntity<?> registerNewUser(@Valid @RequestBody RegisterUserRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        Map<String, String> token = refreshTokenService.refreshToken(refreshTokenRequest.getUserId(), refreshTokenRequest);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.logout(refreshTokenRequest.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
