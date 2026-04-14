package com.wingstars.auth.dto.request;

import com.wingstars.auth.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountUpdateRequest {
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

    @NotBlank
    private String roleCode;

    private Boolean deleted;
}
