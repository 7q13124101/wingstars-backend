package com.wingstars.banner.dto.response;

import com.wingstars.banner.entity.BannerPosition;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BannerResponse {
    private Long id;
    private String title;
    private String linkUrl;
    private BannerPosition positionCode;
    private Integer status;
    private Boolean deleted;
    private List<BannerImageResponse> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
