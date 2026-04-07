package com.wingstars.ranking.repository;

import com.wingstars.ranking.entity.CheerleaderRankingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CheerleaderRankingCategoryRepository extends JpaRepository<CheerleaderRankingCategory, Long> {
    Optional<CheerleaderRankingCategory> findByIdAndIsDeletedFalse(Long id);

    Optional<CheerleaderRankingCategory> findByIdAndStatusTrueAndIsDeletedFalse(Long id);

    Optional<CheerleaderRankingCategory> findByTypeCode(String typeCode);

    boolean existsByTypeCodeAndIsDeletedFalse(String typeCode);

    List<CheerleaderRankingCategory> findByStatusTrueAndIsDeletedFalseOrderByIdDesc();

    List<CheerleaderRankingCategory> findAllByIsDeletedFalseOrderByIdDesc();
}
