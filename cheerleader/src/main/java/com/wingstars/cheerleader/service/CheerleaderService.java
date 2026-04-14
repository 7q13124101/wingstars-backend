package com.wingstars.cheerleader.service;

import com.wingstars.cheerleader.dto.request.CheerleaderRequest;
import com.wingstars.cheerleader.dto.response.CheerleaderResponse;
import com.wingstars.core.payload.PageResponse;

public interface CheerleaderService {
    PageResponse<CheerleaderResponse> getAll(String search, int page, int size);

    CheerleaderResponse getById(Long id);

    CheerleaderResponse create(CheerleaderRequest request);

    CheerleaderResponse update(Long id, CheerleaderRequest request);

    void delete(Long id);
}
