package com.wingstars.cheerleader.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CheerleaderResponse {

    private Long id;

    @JsonProperty("name")
    private String fullName;

    @JsonProperty("number")
    private String jerseyNumber;

    @JsonProperty("social")
    private SocialInfo social;

    @JsonProperty("profile")
    private ProfileInfo profile;

    @JsonProperty("about")
    private String aboutCheerleader;

    @JsonProperty("say")
    private String messageToFans;

    private String avatarUrl;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SocialInfo {
        private String facebook;
        private String instagram;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProfileInfo {
        
        @JsonFormat(pattern = "yyyy-MM-dd")
        @JsonProperty("birthdate")
        private LocalDate birthDate;
        
        @JsonProperty("sign")
        private String zodiacSign;
        
        private String bloodType;
        
        private String height; 
        private String weight;
        
        @JsonProperty("interest")
        private String hobbies;
    }
}
