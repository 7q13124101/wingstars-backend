package com.wingstars.banner.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class BannerImageRequest {
    @NotBlank(message = "Image URL must not be blank")
    private String imageUrl;

    private String linkUrl;

    @Min(value = 0, message = "Display order must be greater than or equal to 0")
    private Integer displayOrder;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Min(value = 0, message = "Image status must be 0 or 1")
    @Max(value = 1, message = "Image status must be 0 or 1")
    private Integer status;
}
