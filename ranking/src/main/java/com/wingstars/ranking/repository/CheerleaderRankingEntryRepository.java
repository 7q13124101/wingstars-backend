package com.wingstars.ranking.repository;

import com.wingstars.ranking.entity.CheerleaderRankingCategory;
import com.wingstars.ranking.entity.CheerleaderRankingEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheerleaderRankingEntryRepository extends JpaRepository<CheerleaderRankingEntry, Long> {
    boolean existsByRankingCategoryIdAndCheerleaderIdAndIsDeletedFalse(Long rankingCategoryId, Long cheerleaderId);

    boolean existsByRankingCategoryIdAndRankPositionAndIsDeletedFalse(Long rankingCategoryId, Integer rankPosition);

    boolean existsByRankingCategoryIdAndRankPositionAndIsDeletedFalseAndIdNot(
            Long rankingCategoryId,
            Integer rankPosition,
            Long id
    );

    java.util.Optional<CheerleaderRankingEntry> findByIdAndIsDeletedFalse(Long id);

    List<CheerleaderRankingEntry> findByRankingCategoryIdAndIsDeletedFalseOrderByRankPositionAsc(Long rankingCategoryId);

    List<CheerleaderRankingEntry> findByRankingCategoryOrderByRankPositionAsc(CheerleaderRankingCategory rankingCategory);
}
