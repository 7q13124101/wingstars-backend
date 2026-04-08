package com.wingstars.cheerleader.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CheerleaderRequest {

    @NotBlank(message = "Full name must not be blank")
    private String fullName;

    private String avatarUrl;
    private String jerseyNumber;
    private String facebookUrl;
    private String instagramUrl;
    private String exclusiveAudioUrl;
    private String photoFrameUrl;
    private String aboutCheerleader;
    private String messageToFans;
    private String hobbies;

    @Positive(message = "Height must be a positive number")
    private Integer heightCm;

    @Positive(message = "Weight must be a positive number")
    private Integer weightKg;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    private String zodiacSign;
    private String bloodType;
}
