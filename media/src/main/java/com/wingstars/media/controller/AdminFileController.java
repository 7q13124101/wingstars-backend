package com.wingstars.media.controller;

import com.wingstars.core.payload.ApiResponse;
import com.wingstars.core.payload.PageResponse;
import com.wingstars.media.dto.MediaUploadResponse;
import com.wingstars.media.enums.ModuleSource;
import com.wingstars.media.service.FileService;
import com.wingstars.media.service.MediaMaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/files")
@RequiredArgsConstructor
@Tag(name = "Admin File", description = "File management APIs for Admin")
public class AdminFileController {

    private final FileService fileService;
    private final MediaMaintenanceService mediaMaintenanceService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Upload file", description = "Upload a new file with module source and optional details.")
    public ResponseEntity<ApiResponse<MediaUploadResponse>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "module_source") ModuleSource moduleSource,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "jump_url", required = false) String jumpUrl,
            @RequestParam(value = "display_order", defaultValue = "0") Integer displayOrder
    ) {
        MediaUploadResponse response = fileService.uploadFile(file, moduleSource, title, jumpUrl, displayOrder);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete file", description = "Soft delete a file by ID.")
    public ResponseEntity<ApiResponse<Void>> deleteFile(@PathVariable Long id) {
        fileService.softDeleteFile(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get file detail", description = "Get full file information by ID.")
    public ResponseEntity<ApiResponse<MediaUploadResponse>> getFileById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(fileService.getFileById(id)));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Replace/Update file", description = "Update file details or replace the physical file.")
    public ResponseEntity<ApiResponse<MediaUploadResponse>> replaceFile(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "jump_url", required = false) String jumpUrl,
            @RequestParam(value = "display_order", required = false) Integer displayOrder,
            @RequestParam(value = "is_active", required = false) Boolean isActive
    ) {
        MediaUploadResponse response = fileService.replaceFile(id, file, title, jumpUrl, displayOrder, isActive);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "List files", description = "Get a paginated list of all non-deleted files.")
    public ResponseEntity<ApiResponse<PageResponse<MediaUploadResponse>>> getFiles(
            @RequestParam(required = false) ModuleSource moduleSource,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort format: field,direction (e.g., id,desc)")
            @RequestParam(defaultValue = "id,desc") String[] sort
    ) {
        String sortField = sort.length > 0 ? sort[0] : "id";
        String sortDirection = sort.length > 1 ? sort[1] : "desc";

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField)
        );

        PageResponse<MediaUploadResponse> result = fileService.getFiles(moduleSource, pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/update-host")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Update media host IP", description = "Prefix all relative media file URLs with the specified host IP. (Super Admin only)")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateMediaHost(
            @Parameter(description = "Must be 1 to execute the update")
            @RequestParam int execute,
            @Parameter(description = "Host URL (e.g., http://10.67.68.111)")
            @RequestParam String host) {
        
        int updatedCount = mediaMaintenanceService.updateFileUrlsWithHost(host, execute);
        
        Map<String, Object> result = new HashMap<>();
        result.put("updatedCount", updatedCount);
        result.put("execute", execute);
        result.put("host", host);
        
        String message = execute == 1 ? "Media hosts updated successfully" : "Execution skipped (execute parameter not 1)";
        
        return ResponseEntity.ok(ApiResponse.success(result, message));
    }
}
