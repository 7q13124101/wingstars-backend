package com.wingstars.media.controller;

import com.wingstars.core.payload.ApiResponse;
import com.wingstars.core.payload.PageResponse;
import com.wingstars.media.dto.MediaUploadResponse;
import com.wingstars.media.enums.ModuleSource;
import com.wingstars.media.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/files")
@RequiredArgsConstructor
@Tag(name = "Public File", description = "Public APIs for viewing Media Assets")
public class PublicFileController {

    private final FileService fileService;

    @GetMapping
    @Operation(summary = "Get public file list", description = "Retrieve a paginated list of active files for the public.")
    public ResponseEntity<ApiResponse<PageResponse<MediaUploadResponse>>> getFiles(
            @RequestParam(required = false) ModuleSource moduleSource,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        PageResponse<MediaUploadResponse> result = fileService.getFiles(moduleSource, pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get file detail", description = "Retrieve basic information of a specific file for the public.")
    public ResponseEntity<ApiResponse<MediaUploadResponse>> getFileById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(fileService.getFileById(id)));
    }
}
