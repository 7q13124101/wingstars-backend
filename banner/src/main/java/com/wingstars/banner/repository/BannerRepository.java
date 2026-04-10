package com.wingstars.banner.repository;

import org.springframework.data.domain.Pageable;
import com.wingstars.banner.entity.Banner; 
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wingstars.banner.dto.BannerDTO;
import java.util.Optional;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    @Query("SELECT new com.wingstars.banner.dto.BannerDTO(b.id, b.title, b.imageUrl, b.linkUrl, b.positionCode, b.displayOrder, b.status, b.createdAt) " +
            "FROM Banner b WHERE b.isDeleted = false")
    Page<BannerDTO> findAllBanners(Pageable pageable);

    @Query("SELECT b FROM Banner b WHERE b.displayOrder = :order AND b.isDeleted = false")
    Optional<Banner> checkDisplayOrder(@Param("order") int order);

    @Query("SELECT b FROM Banner b WHERE b.title = :title AND b.isDeleted = false")
    Optional<Banner> checkTitle(@Param("title") String title);

    @Query("SELECT b FROM Banner b WHERE b.id != :id AND b.title = :title AND b.isDeleted = false")
    Optional<Banner> checkTitleUpdate(@Param("id") Long id, @Param("title") String title);

    @Query("SELECT b FROM Banner b WHERE b.id = :id AND b.isDeleted = false")
    Optional<Banner> findOneId(@Param("id") Long id);
}