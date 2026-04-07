package com.wingstars.cheerleader.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CheerleaderResponse {
    private Long id;
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
    private Integer heightCm;
    private Integer weightKg;
    private LocalDate birthDate;
    private String zodiacSign;
    private String bloodType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
