package com.wingstars.banner.dto.request;

import com.wingstars.banner.entity.BannerPosition;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BannerRequest {
    private String title;

    @Schema(description = "Banner position code", allowableValues = { "HOME_TOP", "EVENT_POPUP",
            "SIDEBAR" }, example = "HOME_TOP")
    private BannerPosition positionCode;

    private Integer durationMs;
    private Integer status;
    private Short displayOrder;

    @NotEmpty(message = "Banner must contain at least one image")
    @Valid
    private List<BannerImageRequest> images;

}
