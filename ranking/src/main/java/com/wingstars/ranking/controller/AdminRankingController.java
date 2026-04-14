package com.wingstars.ranking.controller;

import com.wingstars.core.payload.ApiResponse;
import com.wingstars.ranking.dto.request.RankingCategoryRequest;
import com.wingstars.ranking.dto.request.RankingEntryRequest;
import com.wingstars.ranking.dto.response.RankingCategoryResponse;
import com.wingstars.ranking.dto.response.RankingEntryResponse;
import com.wingstars.ranking.service.RankingCategoryService;
import com.wingstars.ranking.service.RankingEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ranking")
@RequiredArgsConstructor
@Tag(name = "Admin Ranking", description = "Ranking management APIs for Admin")
public class AdminRankingController {

    private final RankingCategoryService categoryService;
    private final RankingEntryService entryService;

    // --- Categories ---

    @GetMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all categories", description = "Get all ranking categories including inactive ones.")
    public ResponseEntity<ApiResponse<List<RankingCategoryResponse>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getAllCategories()));
    }

    @GetMapping("/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get category detail", description = "Get full information of a ranking category by ID.")
    public ResponseEntity<ApiResponse<RankingCategoryResponse>> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getAdminCategoryById(id)));
    }

    @PostMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create category", description = "Create a new ranking category.")
    public ResponseEntity<ApiResponse<RankingCategoryResponse>> createCategory(@Valid @RequestBody RankingCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.create(request)));
    }

    @PutMapping("/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update category", description = "Update ranking category details.")
    public ResponseEntity<ApiResponse<RankingCategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody RankingCategoryRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.update(id, request)));
    }

    @DeleteMapping("/categories/{id}/soft")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft delete category", description = "Deactivate a ranking category.")
    public ResponseEntity<ApiResponse<Void>> softDeleteCategory(@PathVariable Long id) {
        categoryService.softDeleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/categories/{id}/hard")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Hard delete category", description = "Permanently remove a ranking category.")
    public ResponseEntity<ApiResponse<Void>> hardDeleteCategory(@PathVariable Long id) {
        categoryService.hardDeleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // --- Entries ---

    @PostMapping("/categories/{id}/entries")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add cheerleader to rank", description = "Add a cheerleader to a specific ranking category.")
    public ResponseEntity<ApiResponse<RankingEntryResponse>> addIdolToRank(
            @PathVariable("id") Long categoryId,
            @Valid @RequestBody RankingEntryRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(entryService.addIdolToRank(categoryId, request)));
    }

    @PutMapping("/entries/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update ranking entry", description = "Update a cheerleader's rank position or score.")
    public ResponseEntity<ApiResponse<RankingEntryResponse>> updateEntry(
            @PathVariable Long id,
            @Valid @RequestBody RankingEntryRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(entryService.update(id, request)));
    }

    @DeleteMapping("/entries/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove cheerleader from rank", description = "Remove a cheerleader from a ranking category.")
    public ResponseEntity<ApiResponse<Void>> deleteEntry(@PathVariable Long id) {
        entryService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
