package com.wingstars.media.repository;

import com.wingstars.media.entity.MediaAsset;

import com.wingstars.media.enums.ModuleSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaAssetRepository extends JpaRepository<MediaAsset, Long> {
    Optional<MediaAsset> findByIdAndIsDeletedFalse(Long id);

    Page<MediaAsset> findAllByIsDeletedFalse(Pageable pageable);

    Page<MediaAsset> findByModuleSourceAndIsDeletedFalse(ModuleSource moduleSource, Pageable pageable);

    Optional<MediaAsset> findByFileUrl(String fileUrl);
}
