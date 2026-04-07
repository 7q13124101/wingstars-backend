package com.wingstars.ranking.controller;

import com.wingstars.core.payload.ApiResponse;
import com.wingstars.ranking.dto.request.RankingCategoryRequest;
import com.wingstars.ranking.dto.response.RankingCategoryResponse;
import com.wingstars.ranking.service.RankingCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ranking-categories")
@RequiredArgsConstructor
public class RankingCategoryController {
    private final RankingCategoryService rankingCategoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RankingCategoryResponse>>> getActiveCategories() {
        return ResponseEntity.ok(ApiResponse.success(rankingCategoryService.getActiveCategories()));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RankingCategoryResponse>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.success(rankingCategoryService.getAllCategories()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RankingCategoryResponse>> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(rankingCategoryService.getCategoryById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RankingCategoryResponse>> create(
            @Valid @RequestBody RankingCategoryRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(rankingCategoryService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RankingCategoryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody RankingCategoryRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(rankingCategoryService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        rankingCategoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
