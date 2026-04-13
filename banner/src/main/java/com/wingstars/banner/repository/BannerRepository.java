package com.wingstars.banner.repository;

import com.wingstars.banner.entity.Banner;
import com.wingstars.banner.entity.BannerPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    Page<Banner> findByIsDeletedFalse(Pageable pageable);

    Page<Banner> findByIsDeletedTrue(Pageable pageable);

    List<Banner> findByPositionAndStatusAndIsDeletedFalse(BannerPosition position, Integer status);

    @Query("""
            SELECT DISTINCT b
            FROM Banner b
            JOIN FETCH b.images i
            WHERE b.position = :positionCode
              AND b.status = 1
              AND b.isDeleted = false
              AND i.isDeleted = false
              AND (i.startTime IS NULL OR i.startTime <= CURRENT_TIMESTAMP)
              AND (i.endTime IS NULL OR i.endTime >= CURRENT_TIMESTAMP)
            """)
    List<Banner> findActiveBannersByPosition(@Param("positionCode") BannerPosition positionCode);
}
