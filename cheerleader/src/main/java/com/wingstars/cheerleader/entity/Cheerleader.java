package com.wingstars.cheerleader.entity;

import com.wingstars.core.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "cheerleaders",
        indexes = {
                @Index(name = "idx_full_name", columnList = "full_name")
        }
)
public class Cheerleader extends BaseEntity {

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "jersey_number", length = 10)
    private String jerseyNumber;

    @Column(name = "facebook_url", length = 500)
    private String facebookUrl;

    @Column(name = "instagram_url", length = 500)
    private String instagramUrl;

    @Column(name = "exclusive_audio_url", length = 500)
    private String exclusiveAudioUrl;

    @Column(name = "photo_frame_url", length = 500)
    private String photoFrameUrl;

    @Column(name = "about_cheerleader", columnDefinition = "TEXT")
    private String aboutCheerleader;

    @Column(name = "message_to_fans", columnDefinition = "TEXT")
    private String messageToFans;

    @Column(length = 255)
    private String hobbies;

    @Column(name = "height_cm")
    private Integer heightCm;

    @Column(name = "weight_kg")
    private Integer weightKg;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "zodiac_sign", length = 50)
    private String zodiacSign;

    @Column(name = "blood_type", length = 10)
    private String bloodType;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isDeleted = false;
}
