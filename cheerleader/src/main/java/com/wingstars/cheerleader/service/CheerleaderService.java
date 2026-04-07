package com.wingstars.cheerleader.service;

import com.wingstars.cheerleader.dto.request.CheerleaderRequest;
import com.wingstars.cheerleader.dto.response.CheerleaderResponse;
import org.springframework.data.domain.Page;

public interface CheerleaderService {
    Page<CheerleaderResponse> getAll(String search, int page, int size);

    CheerleaderResponse getById(Long id);

    CheerleaderResponse create(CheerleaderRequest request);

    CheerleaderResponse update(Long id, CheerleaderRequest request);

    void delete(Long id);
}
