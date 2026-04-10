package com.wingstars.banner.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wingstars.banner.dto.BannerDTO;
import com.wingstars.banner.service.BannerService;
import com.wingstars.core.common.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {
    
    private final BannerService bannerService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BannerDTO>> getBannerById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BannerDTO>> addBanner(@RequestBody BannerDTO bannerDTO) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.add(bannerDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BannerDTO>> updateBanner(@PathVariable Long id, @RequestBody BannerDTO bannerDTO) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.update(bannerDTO, id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBanner(@PathVariable Long id) {
        bannerService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BannerDTO>>> getAllBanners(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getAll(page, pageSize)));
    }
}