package com.wingstars.banner.service;

import org.springframework.data.domain.Page;
import com.wingstars.banner.dto.BannerDTO;

public interface BannerService {
    BannerDTO add(BannerDTO bannerDTO); 
    BannerDTO update(BannerDTO bannerDTO, Long id);
    void delete(Long id);
    Page<BannerDTO> getAll(int page, int pageSize);
    BannerDTO getById(Long id);
}