package com.wingstars.media.service;

import com.wingstars.media.entity.MediaAsset;
import com.wingstars.media.repository.MediaAssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String uploadFile(MultipartFile file, String moduleSource) {
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
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(storedName).normalize();
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String fileUrl = "/uploads/" + storedName;

            MediaAsset asset = MediaAsset.builder()
                    .fileUrl(fileUrl)
                    .moduleSource(moduleSource)
                    .title(originalFilename)
                    .isActive(true)
                    .isDeleted(false)
                    .build();
            mediaAssetRepository.save(asset);

            return fileUrl;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file. Please try again!", ex);
        }
    }
}
