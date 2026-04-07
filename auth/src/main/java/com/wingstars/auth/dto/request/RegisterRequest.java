package com.wingstars.auth.dto.request;

import com.wingstars.auth.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    @Size(max = 100)
    private String username;

    @NotBlank
    @Size(min = 6, max = 255)
    private String password;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String fullName;

    @Size(max = 20)
    private String phone;

    private Gender gender;

    @Size(max = 255)
    private String address;

    @Size(max = 500)
    private String imageUrl;
}
