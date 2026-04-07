package com.wingstars.ranking.service.impl;

import com.wingstars.cheerleader.entity.Cheerleader;
import com.wingstars.cheerleader.repository.CheerleaderRepository;
import com.wingstars.core.exception.BusinessException;
import com.wingstars.ranking.dto.request.RankingEntryRequest;
import com.wingstars.ranking.dto.response.RankingEntryResponse;
import com.wingstars.ranking.entity.CheerleaderRankingCategory;
import com.wingstars.ranking.entity.CheerleaderRankingEntry;
import com.wingstars.ranking.repository.CheerleaderRankingCategoryRepository;
import com.wingstars.ranking.repository.CheerleaderRankingEntryRepository;
import com.wingstars.ranking.service.RankingEntryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingEntryServiceImpl implements RankingEntryService {
    private final CheerleaderRankingCategoryRepository categoryRepository;
    private final CheerleaderRankingEntryRepository entryRepository;
    private final CheerleaderRepository cheerleaderRepository;

    @Override
    @Transactional
    public RankingEntryResponse addIdolToRank(Long categoryId, RankingEntryRequest request) {
        CheerleaderRankingCategory category = categoryRepository.findByIdAndIsDeletedFalse(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category does not exist or has been deleted"));

        Cheerleader idol = cheerleaderRepository.findByIdAndIsDeletedFalse(request.getCheerleaderId())
                .orElseThrow(() -> new EntityNotFoundException("Cheerleader does not exist or has been deleted"));

        if (entryRepository.existsByRankingCategoryIdAndCheerleaderIdAndIsDeletedFalse(categoryId, idol.getId())) {
            throw new BusinessException("Cheerleader already exists in this ranking category");
        }

        if (entryRepository.existsByRankingCategoryIdAndRankPositionAndIsDeletedFalse(categoryId, request.getRankPosition())) {
            throw new BusinessException("Rank position " + request.getRankPosition() + " is already occupied");
        }

        CheerleaderRankingEntry entry = CheerleaderRankingEntry.builder()
                .rankingCategory(category)
                .cheerleader(idol)
                .rankPosition(request.getRankPosition())
                .score(request.getScore() == null ? 0 : request.getScore())
                .cheerleaderImageUrl(request.getCheerleaderImageUrl())
                .isDeleted(false)
                .build();

        return mapToResponse(entryRepository.save(entry));
    }

    @Override
    @Transactional
    public RankingEntryResponse update(Long id, RankingEntryRequest request) {
        CheerleaderRankingEntry entry = entryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Ranking entry does not exist or has been deleted"));

        if (!entry.getCheerleader().getId().equals(request.getCheerleaderId())) {
            throw new BusinessException("Changing cheerleader in an existing ranking entry is not supported");
        }

        if (entryRepository.existsByRankingCategoryIdAndRankPositionAndIsDeletedFalseAndIdNot(
                entry.getRankingCategory().getId(),
                request.getRankPosition(),
                entry.getId()
        )) {
            throw new BusinessException("Rank position " + request.getRankPosition() + " is already occupied");
        }

        entry.setRankPosition(request.getRankPosition());
        entry.setScore(request.getScore() == null ? 0 : request.getScore());
        entry.setCheerleaderImageUrl(request.getCheerleaderImageUrl());

        return mapToResponse(entryRepository.save(entry));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CheerleaderRankingEntry entry = entryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Ranking entry does not exist or has been deleted"));

        entry.setIsDeleted(true);
        entryRepository.save(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RankingEntryResponse> getEntriesByCategoryId(Long categoryId) {
        categoryRepository.findByIdAndIsDeletedFalse(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category does not exist or has been deleted"));

        return entryRepository.findByRankingCategoryIdAndIsDeletedFalseOrderByRankPositionAsc(categoryId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private RankingEntryResponse mapToResponse(CheerleaderRankingEntry entry) {
        return RankingEntryResponse.builder()
                .id(entry.getId())
                .rankPosition(entry.getRankPosition())
                .score(entry.getScore())
                .rankingImageUrl(entry.getCheerleaderImageUrl())
                .cheerleaderId(entry.getCheerleader().getId())
                .cheerleaderName(entry.getCheerleader().getFullName())
                .defaultAvatarUrl(entry.getCheerleader().getAvatarUrl())
                .build();
    }
}
