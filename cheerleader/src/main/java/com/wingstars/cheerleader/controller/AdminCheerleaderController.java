package com.wingstars.cheerleader.controller;

import com.wingstars.cheerleader.dto.request.CheerleaderRequest;
import com.wingstars.cheerleader.dto.response.CheerleaderResponse;
import com.wingstars.cheerleader.service.CheerleaderService;
import com.wingstars.core.payload.ApiResponse;
import com.wingstars.core.payload.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/cheerleaders")
@RequiredArgsConstructor
@Tag(name = "Admin Cheerleader", description = "Management APIs for Cheerleaders")
public class AdminCheerleaderController {

    private final CheerleaderService cheerleaderService;

    @GetMapping
    @Operation(summary = "Get all cheerleaders", description = "Retrieve a paginated list of all cheerleaders for administration.")
    public ResponseEntity<ApiResponse<PageResponse<CheerleaderResponse>>> getAll(
            @Parameter(description = "Search by full name")
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(cheerleaderService.getAll(search, page, size)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get cheerleader by ID", description = "Retrieve full details of a cheerleader by their ID.")
    public ResponseEntity<ApiResponse<CheerleaderResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(cheerleaderService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create cheerleader", description = "Add a new cheerleader to the system.")
    public ResponseEntity<ApiResponse<CheerleaderResponse>> create(@Valid @RequestBody CheerleaderRequest request) {
        return ResponseEntity.ok(ApiResponse.success(cheerleaderService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update cheerleader", description = "Update existing cheerleader information.")
    public ResponseEntity<ApiResponse<CheerleaderResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CheerleaderRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(cheerleaderService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete cheerleader", description = "Soft delete a cheerleader from the system.")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        cheerleaderService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
