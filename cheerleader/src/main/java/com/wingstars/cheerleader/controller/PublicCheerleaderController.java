package com.wingstars.cheerleader.controller;

import com.wingstars.cheerleader.dto.response.CheerleaderResponse;
import com.wingstars.cheerleader.service.CheerleaderService;
import com.wingstars.core.payload.ApiResponse;
import com.wingstars.core.payload.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/cheerleaders")
@RequiredArgsConstructor
@Tag(name = "Public Cheerleader", description = "Public APIs for viewing Cheerleaders")
public class PublicCheerleaderController {

    private final CheerleaderService cheerleaderService;

    @GetMapping
    @Operation(summary = "Get cheerleader list", description = "Retrieve a paginated list of active cheerleaders for the public.")
    public ResponseEntity<ApiResponse<PageResponse<CheerleaderResponse>>> getAll(
            @Parameter(description = "Search by full name")
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(cheerleaderService.getAll(search, page, size)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get cheerleader detail", description = "Retrieve details of a specific cheerleader for the public.")
    public ResponseEntity<ApiResponse<CheerleaderResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(cheerleaderService.getById(id)));
    }
}
