package com.wingstars.ranking.repository;

import com.wingstars.ranking.entity.CheerleaderRankingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CheerleaderRankingCategoryRepository extends JpaRepository<CheerleaderRankingCategory, Long> {

    Optional<CheerleaderRankingCategory> findByIdAndStatusTrue(Long id);

    Optional<CheerleaderRankingCategory> findByTypeCode(String typeCode);

    boolean existsByTypeCode(String typeCode);

    List<CheerleaderRankingCategory> findByStatusTrueOrderByIdDesc();

    List<CheerleaderRankingCategory> findAllByOrderByIdDesc();
}
