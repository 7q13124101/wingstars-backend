package com.wingstars.media.controller;

import com.wingstars.core.payload.ApiResponse;
import com.wingstars.media.dto.MediaUploadResponse;
import com.wingstars.media.enums.ModuleSource;
import com.wingstars.media.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<MediaUploadResponse>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "module_source", required = false) ModuleSource moduleSource
    ) {
        MediaUploadResponse response = fileService.uploadFile(file, moduleSource);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
