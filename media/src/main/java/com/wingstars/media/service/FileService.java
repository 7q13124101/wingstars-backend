package com.wingstars.media.service;

import com.wingstars.media.dto.MediaUploadResponse;
import com.wingstars.media.entity.MediaAsset;
import com.wingstars.media.enums.ModuleSource;
import com.wingstars.media.repository.MediaAssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "pdf");

    private final MediaAssetRepository mediaAssetRepository;

    @Value("${file.upload-dir:upload/}")
    private String uploadDir;

    public MediaUploadResponse uploadFile(
            MultipartFile file,
            ModuleSource moduleSource,
            String title,
            String jumpUrl,
            Integer displayOrder
    ) {
        StoredFile storedFile = storePhysicalFile(file, moduleSource);

        try {
            MediaAsset asset = MediaAsset.builder()
                    .fileUrl(storedFile.fileUrl())
                    .moduleSource(moduleSource)
                    .title(StringUtils.hasText(title) ? title : storedFile.originalFilename())
                    .jumpUrl(jumpUrl)
                    .displayOrder(displayOrder == null ? 0 : displayOrder)
                    .isActive(true)
                    .isDeleted(false)
                    .build();
            asset = mediaAssetRepository.save(asset);

            return toUploadResponse(asset);
        } catch (RuntimeException ex) {
            deletePhysicalFile(storedFile.fileUrl());
            throw ex;
        }
    }

    @Transactional
    public void softDeleteFile(Long id) {
        MediaAsset asset = mediaAssetRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("File does not exist or has already been deleted"));

        asset.setIsDeleted(true);
        mediaAssetRepository.save(asset);
    }

    public Page<MediaUploadResponse> getFiles(ModuleSource moduleSource, Pageable pageable) {
        Page<MediaAsset> assets;

        if (moduleSource != null) {
            assets = mediaAssetRepository.findByModuleSourceAndIsDeletedFalse(moduleSource, pageable);
        } else {
            assets = mediaAssetRepository.findAllByIsDeletedFalse(pageable);
        }

        return assets.map(this::toUploadResponse);
    }

    public MediaUploadResponse getFileById(Long id) {
        MediaAsset asset = mediaAssetRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("File does not exist or has already been deleted"));

        return toUploadResponse(asset);
    }

    @Transactional
    public MediaUploadResponse replaceFile(
            Long id,
            MultipartFile newFile,
            String title,
            String jumpUrl,
            Integer displayOrder,
            Boolean isActive
    ) {
        MediaAsset asset = mediaAssetRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("File does not exist"));

        if (newFile != null && !newFile.isEmpty()) {
            String oldFileUrl = asset.getFileUrl();
            StoredFile storedFile = storePhysicalFile(newFile, asset.getModuleSource());
            deletePhysicalFile(oldFileUrl);

            asset.setFileUrl(storedFile.fileUrl());
            if (!StringUtils.hasText(title)) {
                asset.setTitle(storedFile.originalFilename());
            }
        }

        if (StringUtils.hasText(title)) {
            asset.setTitle(title);
        }
        if (jumpUrl != null) {
            asset.setJumpUrl(jumpUrl);
        }
        if (displayOrder != null) {
            asset.setDisplayOrder(displayOrder);
        }
        if (isActive != null) {
            asset.setIsActive(isActive);
        }

        asset = mediaAssetRepository.save(asset);

        return toUploadResponse(asset);
    }

    private MediaUploadResponse toUploadResponse(MediaAsset asset) {
        return MediaUploadResponse.builder()
                .mediaId(asset.getId())
                .fileUrl(asset.getFileUrl())
                .moduleSource(asset.getModuleSource() == null ? null : asset.getModuleSource().name())
                .title(asset.getTitle())
                .jumpUrl(asset.getJumpUrl())
                .displayOrder(asset.getDisplayOrder())
                .isActive(asset.getIsActive())
                .build();
    }

    private StoredFile storePhysicalFile(MultipartFile file, ModuleSource moduleSource) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) {
            throw new RuntimeException("File name is invalid");
        }

        String extension = StringUtils.getFilenameExtension(originalFilename);
        if (!StringUtils.hasText(extension)) {
            throw new RuntimeException("File extension is missing");
        }

        extension = extension.toLowerCase(Locale.ROOT);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new RuntimeException("File format not allowed");
        }

        String storedName = UUID.randomUUID() + "." + extension;
        String folderName = resolveFolderName(moduleSource);
        Path uploadPath = Paths.get(uploadDir, folderName).toAbsolutePath().normalize();

        try {
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(storedName).normalize();
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String fileUrl = "/uploads/" + folderName + "/" + storedName;
            return new StoredFile(fileUrl, originalFilename);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file. Please try again!", ex);
        }
    }

    private void deletePhysicalFile(String fileUrl) {
        if (!StringUtils.hasText(fileUrl)) {
            return;
        }

        try {
            String relativePath = fileUrl.startsWith("/uploads/") ? fileUrl.substring("/uploads/".length()) : fileUrl;
            Path filePath = Paths.get(uploadDir).resolve(relativePath).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            System.err.println("Could not delete old physical file: " + fileUrl);
        }
    }

    private String resolveFolderName(ModuleSource moduleSource) {
        if (moduleSource == null) {
            return "common";
        }

        return moduleSource.name()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9_-]", "_");
    }

    private record StoredFile(String fileUrl, String originalFilename) {
    }
}
