package com.wingstars.banner.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Supported banner positions")
public enum BannerPosition {
    @Schema(description = "Banner shown at the top of the home page")
    HOME_TOP,
    @Schema(description = "Popup banner for events or campaigns")
    EVENT_POPUP,
    @Schema(description = "Banner shown in the sidebar area")
    SIDEBAR
}
