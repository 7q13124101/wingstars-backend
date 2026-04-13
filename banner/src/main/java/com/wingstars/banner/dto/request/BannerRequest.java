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

    private String linkUrl;

    @Schema(description = "Banner position code", allowableValues = { "HOME_TOP", "EVENT_POPUP",
            "SIDEBAR" }, example = "HOME_TOP")
    private BannerPosition positionCode;

    @Min(value = 0, message = "Status must be 0 or 1")
    @Max(value = 1, message = "Status must be 0 or 1")
    private Integer status;

    @NotEmpty(message = "Banner must contain at least one image")
    @Valid
    private List<BannerImageRequest> images;

}
