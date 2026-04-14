package com.wingstars.banner.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BannerImageResponse {
    private Long id;
    private String imageUrl;
    private String linkUrl;
    private Integer displayOrder;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
