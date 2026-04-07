package com.wingstars.ranking.service;

import com.wingstars.ranking.dto.request.RankingEntryRequest;
import com.wingstars.ranking.dto.response.RankingEntryResponse;

public interface RankingEntryService {
    RankingEntryResponse addIdolToRank(Long categoryId, RankingEntryRequest request);

    RankingEntryResponse update(Long id, RankingEntryRequest request);

    void delete(Long id);

    java.util.List<RankingEntryResponse> getEntriesByCategoryId(Long categoryId);
}
