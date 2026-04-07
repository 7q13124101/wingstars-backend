package com.wingstars.cheerleader.controller;

import com.wingstars.cheerleader.dto.request.CheerleaderRequest;
import com.wingstars.cheerleader.dto.response.CheerleaderResponse;
import com.wingstars.cheerleader.service.CheerleaderService;
import com.wingstars.core.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cheerleaders")
@RequiredArgsConstructor
public class CheerleaderController {

    private final CheerleaderService cheerleaderService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CheerleaderResponse>>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(cheerleaderService.getAll(search, page, size)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CheerleaderResponse>> create(@Valid @RequestBody CheerleaderRequest request) {
        return ResponseEntity.ok(ApiResponse.success(cheerleaderService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CheerleaderResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CheerleaderRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(cheerleaderService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        cheerleaderService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
