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

    @NotBlank(message = "Tên Idol không được để trống")
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

    @Positive(message = "Chiều cao phải là số dương")
    private Integer heightCm;

    @Positive(message = "Cân nặng phải là số dương")
    private Integer weightKg;

    @Past(message = "Ngày sinh phải ở trong quá khứ")
    private LocalDate birthDate;

    private String zodiacSign;
    private String bloodType;
}
