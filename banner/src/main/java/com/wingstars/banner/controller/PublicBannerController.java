package com.wingstars.banner.controller;

import com.wingstars.banner.dto.response.BannerResponse;
import com.wingstars.banner.entity.BannerPosition;
import com.wingstars.banner.service.BannerService;
import com.wingstars.core.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/banners")
@RequiredArgsConstructor
@Tag(name = "Public Banner", description = "Public APIs for displaying banners")
public class PublicBannerController {
    private final BannerService bannerService;

    @GetMapping
    @Operation(summary = "Get active banner by position", description = "Return the single active banner for the requested position.")
    public ResponseEntity<ApiResponse<BannerResponse>> getActiveByPosition(
            @Parameter(
                    description = "Banner position code",
                    example = "HOME_TOP"
            )
            @RequestParam BannerPosition positionCode
    ) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getPublicBanner(positionCode)));
    }
}
