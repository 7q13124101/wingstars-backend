package com.wingstars.banner.service;

import com.wingstars.banner.dto.request.BannerRequest;
import com.wingstars.banner.dto.response.BannerResponse;
import com.wingstars.banner.entity.BannerPosition;
import com.wingstars.core.payload.PageResponse;

public interface BannerService {
    BannerResponse create(BannerRequest request);

    BannerResponse update(Long id, BannerRequest request);

    BannerResponse updateStatus(Long id, Integer status);

    BannerResponse restore(Long id);

    void softDelete(Long id);

    void hardDelete(Long id);

    BannerResponse getAdminById(Long id);

    PageResponse<BannerResponse> getAdminBanners(int page, int pageSize);

    PageResponse<BannerResponse> getTrash(int page, int pageSize);

    BannerResponse getPublicBanner(BannerPosition positionCode);
}
