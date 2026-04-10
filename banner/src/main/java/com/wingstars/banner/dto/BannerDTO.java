package com.wingstars.banner.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BannerDTO {
    private Long id;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private String positionCode;
    private Integer displayOrder;
    private Integer status;
    private LocalDateTime createdAt;
}