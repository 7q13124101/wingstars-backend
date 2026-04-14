package com.wingstars.banner.controller;

import com.wingstars.banner.dto.request.BannerRequest;
import com.wingstars.banner.dto.response.BannerResponse;
import com.wingstars.banner.service.BannerService;
import com.wingstars.core.payload.ApiResponse;
import com.wingstars.core.payload.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/banners")
@RequiredArgsConstructor
@Tag(name = "Admin Banner", description = "Admin APIs for managing banners")
public class AdminBannerController {
    private final BannerService bannerService;

    @PostMapping
    @Operation(summary = "Create banner", description = "Create a new banner. positionCode: HOME_TOP or SIDEBAR or EVENT_POPUP")
    public ResponseEntity<ApiResponse<BannerResponse>> create(@Valid @RequestBody BannerRequest request) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.create(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update banner", description = "Update banner information and images.")
    public ResponseEntity<ApiResponse<BannerResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody BannerRequest request) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.update(id, request)));
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore banner", description = "Restore a banner from trash.")
    public ResponseEntity<ApiResponse<BannerResponse>> restore(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.restore(id)));
    }

    @DeleteMapping("/{id}/soft")
    @Operation(summary = "Move banner to trash", description = "Soft delete a banner and hide it from public APIs.")
    public ResponseEntity<ApiResponse<Void>> softDelete(@PathVariable Long id) {
        bannerService.softDelete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{id}/hard")
    @Operation(summary = "Delete banner permanently", description = "Permanently delete a banner and its images.")
    public ResponseEntity<ApiResponse<Void>> hardDelete(@PathVariable Long id) {
        bannerService.hardDelete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get banner detail", description = "Get full banner information by id for admin use.")
    public ResponseEntity<ApiResponse<BannerResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getAdminById(id)));
    }

    @GetMapping
    @Operation(summary = "Get list for admin", description = "Get paginated banner list excluding trashed banners.")
    public ResponseEntity<ApiResponse<PageResponse<BannerResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getAdminBanners(page, pageSize)));
    }

    @GetMapping("/trash")
    @Operation(summary = "Get trash list", description = "Get paginated banner list from trash.")
    public ResponseEntity<ApiResponse<PageResponse<BannerResponse>>> getTrash(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(ApiResponse.success(bannerService.getTrash(page, pageSize)));
    }
}
