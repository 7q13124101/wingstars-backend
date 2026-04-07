package com.wingstars.ranking.controller;

import com.wingstars.core.payload.ApiResponse;
import com.wingstars.ranking.dto.response.RankingCategoryResponse;
import com.wingstars.ranking.service.RankingCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/ranking-categories")
@RequiredArgsConstructor
public class AdminRankingCategoryController {
    private final RankingCategoryService rankingCategoryService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RankingCategoryResponse>> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(rankingCategoryService.getAdminCategoryById(id)));
    }

    @DeleteMapping("/{id}/soft")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> softDelete(@PathVariable Long id) {
        rankingCategoryService.softDeleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{id}/hard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> hardDelete(@PathVariable Long id) {
        rankingCategoryService.hardDeleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
