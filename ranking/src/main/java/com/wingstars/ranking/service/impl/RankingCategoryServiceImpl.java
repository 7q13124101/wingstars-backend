package com.wingstars.ranking.service.impl;

import com.wingstars.ranking.dto.request.RankingCategoryRequest;
import com.wingstars.ranking.dto.response.RankingCategoryResponse;
import com.wingstars.ranking.entity.CheerleaderRankingCategory;
import com.wingstars.ranking.repository.CheerleaderRankingCategoryRepository;
import com.wingstars.ranking.service.RankingCategoryService;
import com.wingstars.core.exception.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingCategoryServiceImpl implements RankingCategoryService {
    private final CheerleaderRankingCategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RankingCategoryResponse> getActiveCategories() {
        return categoryRepository.findByStatusTrueAndIsDeletedFalseOrderByIdDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RankingCategoryResponse> getAllCategories() {
        return categoryRepository.findAllByIsDeletedFalseOrderByIdDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RankingCategoryResponse getCategoryById(Long id) {
        return toResponse(findActiveCategory(id));
    }

    @Override
    @Transactional(readOnly = true)
    public RankingCategoryResponse getAdminCategoryById(Long id) {
        return toResponse(findCategory(id));
    }

    @Override
    @Transactional
    public RankingCategoryResponse create(RankingCategoryRequest request) {
        validateTypeCodeUniqueness(request.getTypeCode(), null);

        CheerleaderRankingCategory category = modelMapper.map(request, CheerleaderRankingCategory.class);
        if (category.getStatus() == null) {
            category.setStatus(true);
        }
        return toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public RankingCategoryResponse update(Long id, RankingCategoryRequest request) {
        CheerleaderRankingCategory category = findCategory(id);
        validateTypeCodeUniqueness(request.getTypeCode(), id);

        category.setName(request.getName());
        category.setTypeCode(request.getTypeCode());
        if (request.getStatus() != null) {
            category.setStatus(request.getStatus());
        }

        return toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        softDeleteCategory(id);
    }

    @Override
    @Transactional
    public void softDeleteCategory(Long id) {
        CheerleaderRankingCategory category = findCategory(id);
        category.setIsDeleted(true);
        category.getEntries().forEach(entry -> entry.setIsDeleted(true));
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void hardDeleteCategory(Long id) {
        CheerleaderRankingCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ranking category does not exist"));
        categoryRepository.delete(category);
    }

    private CheerleaderRankingCategory findCategory(Long id) {
        return categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Ranking category does not exist"));
    }

    private CheerleaderRankingCategory findActiveCategory(Long id) {
        return categoryRepository.findByIdAndStatusTrueAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Active ranking category does not exist"));
    }

    private void validateTypeCodeUniqueness(String typeCode, Long currentId) {
        if (!StringUtils.hasText(typeCode)) {
            return;
        }

        String normalizedTypeCode = typeCode.trim();
        categoryRepository.findByTypeCode(normalizedTypeCode)
                .filter(category -> !category.getId().equals(currentId))
                .ifPresent(category -> {
                    throw new BusinessException("Ma phan loai '" + normalizedTypeCode + "' da ton tai trong he thong");
                });
    }

    private RankingCategoryResponse toResponse(CheerleaderRankingCategory category) {
        return modelMapper.map(category, RankingCategoryResponse.class);
    }
}
