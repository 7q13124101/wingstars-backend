package com.wingstars.core.dto;

import lombok.Data;

@Data
public class CheerleaderJsonDTO {
    private Long id;
    private String name;
    private String number;
    private SocialDTO social;
    private ProfileDTO profile;
    private String about;
    private String say;
    private String avatarUrl;
}
