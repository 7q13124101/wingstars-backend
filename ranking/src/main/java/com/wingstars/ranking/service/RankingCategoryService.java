package com.wingstars.ranking.service;

import com.wingstars.ranking.dto.request.RankingCategoryRequest;
import com.wingstars.ranking.dto.response.RankingCategoryResponse;

import java.util.List;

public interface RankingCategoryService {
    List<RankingCategoryResponse> getActiveCategories();

    List<RankingCategoryResponse> getAllCategories();

    RankingCategoryResponse getCategoryById(Long id);

    RankingCategoryResponse getAdminCategoryById(Long id);

    RankingCategoryResponse create(RankingCategoryRequest request);

    RankingCategoryResponse update(Long id, RankingCategoryRequest request);

    void delete(Long id);

    void hardDeleteCategory(Long id);
}
