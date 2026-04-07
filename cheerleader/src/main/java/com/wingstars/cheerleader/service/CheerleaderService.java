package com.wingstars.cheerleader.service;

import com.wingstars.cheerleader.dto.request.CheerleaderRequest;
import com.wingstars.cheerleader.entity.Cheerleader;
import org.springframework.data.domain.Page;

public interface CheerleaderService {
    Page<Cheerleader> getAll(String search, int page, int size);

    Cheerleader create(CheerleaderRequest request);

    Cheerleader update(Long id, CheerleaderRequest request);

    void delete(Long id);
}
