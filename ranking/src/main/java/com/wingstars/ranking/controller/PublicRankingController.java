package com.wingstars.ranking.controller;

import com.wingstars.core.payload.ApiResponse;
import com.wingstars.ranking.dto.response.RankingCategoryResponse;
import com.wingstars.ranking.dto.response.RankingEntryResponse;
import com.wingstars.ranking.service.RankingCategoryService;
import com.wingstars.ranking.service.RankingEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/ranking")
@RequiredArgsConstructor
@Tag(name = "Public Ranking", description = "Public APIs for viewing Rankings")
public class PublicRankingController {

    private final RankingCategoryService categoryService;
    private final RankingEntryService entryService;

    @GetMapping("/categories")
    @Operation(summary = "List active categories", description = "Get all active ranking categories.")
    public ResponseEntity<ApiResponse<List<RankingCategoryResponse>>> getActiveCategories() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getActiveCategories()));
    }

    @GetMapping("/categories/{id}")
    @Operation(summary = "Get category detail", description = "Get basic information of a ranking category.")
    public ResponseEntity<ApiResponse<RankingCategoryResponse>> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getCategoryById(id)));
    }

    @GetMapping("/categories/{id}/entries")
    @Operation(summary = "List entries in category", description = "Get the list of cheerleaders in a specific ranking category.")
    public ResponseEntity<ApiResponse<List<RankingEntryResponse>>> getEntriesByCategoryId(@PathVariable("id") Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(entryService.getEntriesByCategoryId(categoryId)));
    }
}
