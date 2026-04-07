package com.wingstars.ranking.controller;

import com.wingstars.core.payload.ApiResponse;
import com.wingstars.ranking.dto.request.RankingEntryRequest;
import com.wingstars.ranking.dto.response.RankingEntryResponse;
import com.wingstars.ranking.service.RankingEntryService;
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
@RequiredArgsConstructor
public class RankingEntryController {
    private final RankingEntryService rankingEntryService;

    @PostMapping("/api/ranking-categories/{id}/entries")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RankingEntryResponse>> addIdolToRank(
            @PathVariable("id") Long categoryId,
            @Valid @RequestBody RankingEntryRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(rankingEntryService.addIdolToRank(categoryId, request)));
    }

    @PutMapping("/api/ranking-entries/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RankingEntryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody RankingEntryRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(rankingEntryService.update(id, request)));
    }

    @DeleteMapping("/api/ranking-entries/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        rankingEntryService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/api/ranking-categories/{id}/entries")
    public ResponseEntity<ApiResponse<List<RankingEntryResponse>>> getEntriesByCategoryId(@PathVariable("id") Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(rankingEntryService.getEntriesByCategoryId(categoryId)));
    }
}
