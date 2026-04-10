package com.wingstars.banner.service.Impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.wingstars.banner.Mapper.BannerMapper;
import com.wingstars.banner.dto.BannerDTO;
import com.wingstars.banner.entity.Banner;
import com.wingstars.banner.repository.BannerRepository;
import com.wingstars.banner.service.BannerService;
import com.wingstars.core.common.NotFoundException;
import com.wingstars.core.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IBannerService implements BannerService {
    
    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    @Override
    public BannerDTO add(BannerDTO bannerDTO) {
        bannerRepository.checkTitle(bannerDTO.getTitle()).ifPresent(b -> {
            throw new BusinessException("Title already exists");
        });

        if(bannerDTO.getImageUrl() == null || bannerDTO.getImageUrl().isEmpty()){
            throw new BusinessException("Image URL cannot be empty");
        }

        Banner banner = bannerMapper.toEntity(bannerDTO);
        banner.setIsDeleted(false);
        banner.setStatus(bannerDTO.getStatus() != null ? bannerDTO.getStatus() : 1);
        banner.setDisplayOrder(bannerDTO.getDisplayOrder() != null ? bannerDTO.getDisplayOrder() : 0);
        
        banner = bannerRepository.save(banner);
        return bannerMapper.toDTO(banner);
    }

    @Override
    public BannerDTO update(BannerDTO bannerDTO, Long id) {
        Banner existingBanner = bannerRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Banner not found"));
            
        if(Boolean.TRUE.equals(existingBanner.getIsDeleted())) {
            throw new BusinessException("Banner has been deleted");
        }

        if(bannerDTO.getTitle() != null && !bannerDTO.getTitle().isEmpty()){
            bannerRepository.checkTitleUpdate(id, bannerDTO.getTitle()).ifPresent(b -> {
                throw new BusinessException("Title already exists");
            });
            existingBanner.setTitle(bannerDTO.getTitle());
        }

        if(bannerDTO.getImageUrl() != null && !bannerDTO.getImageUrl().isEmpty()){
            existingBanner.setImageUrl(bannerDTO.getImageUrl());
        }

        if(bannerDTO.getLinkUrl() != null){
            existingBanner.setLinkUrl(bannerDTO.getLinkUrl());
        }
        if(bannerDTO.getDisplayOrder() != null){
            existingBanner.setDisplayOrder(bannerDTO.getDisplayOrder());
        }
        if(bannerDTO.getPositionCode() != null){
            existingBanner.setPositionCode(bannerDTO.getPositionCode());
        }
        if(bannerDTO.getStatus() != null){
            existingBanner.setStatus(bannerDTO.getStatus());
        }

        existingBanner = bannerRepository.save(existingBanner);
        
        return bannerMapper.toDTO(existingBanner);
    }

    @Override
    public void delete(Long id) {
        Banner banner = bannerRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Banner not found"));
        
        banner.setIsDeleted(true);
        bannerRepository.save(banner);
    }

    @Override
    public BannerDTO getById(Long id) {
        Banner banner = bannerRepository.findOneId(id)
            .orElseThrow(() -> new NotFoundException("Banner not found"));
        return bannerMapper.toDTO(banner);
    }

    @Override
    public Page<BannerDTO> getAll(int page, int pageSize) {
        // Adjust page to be zero-indexed if the frontend sends 1-indexed
        int pageIndex = page > 0 ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return bannerRepository.findAllBanners(pageable);
    }
}