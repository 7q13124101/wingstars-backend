package com.wingstars.banner.service.impl;

import com.wingstars.banner.dto.request.BannerImageRequest;
import com.wingstars.banner.dto.request.BannerRequest;
import com.wingstars.banner.dto.response.BannerImageResponse;
import com.wingstars.banner.dto.response.BannerResponse;
import com.wingstars.banner.entity.Banner;
import com.wingstars.banner.entity.BannerImage;
import com.wingstars.banner.entity.BannerPosition;
import com.wingstars.banner.repository.BannerRepository;
import com.wingstars.banner.service.BannerService;
import com.wingstars.core.exception.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {
    private static final int DEFAULT_IMAGE_DURATION_MS = 3000;
    private static final BannerPosition DEFAULT_POSITION_CODE = BannerPosition.HOME_TOP;
    private static final int INACTIVE_STATUS = 0;
    private static final int ACTIVE_STATUS = 1;

    private final BannerRepository bannerRepository;

    @Override
    @Transactional
    public BannerResponse create(BannerRequest request) {
        Banner banner = new Banner();
        banner.setTitle(trimToNull(request.getTitle()));
        banner.setLinkUrl(trimToNull(request.getLinkUrl()));
        banner.setPositionCode(defaultPositionCode(request.getPositionCode()));
        banner.setStatus(defaultCreateStatus(request.getStatus()));
        banner.setIsDeleted(false);
        replaceImages(banner, request.getImages());
        if (banner.getStatus() == ACTIVE_STATUS) {
            deactivateOtherActiveBanners(banner.getPositionCode(), null);
        }

        return toResponse(bannerRepository.save(banner));
    }

    @Override
    @Transactional
    public BannerResponse update(Long id, BannerRequest request) {
        Banner banner = getExistingBanner(id);
        BannerPosition newPositionCode = defaultPositionCode(request.getPositionCode());
        int newStatus = resolveUpdatedStatus(request.getStatus(), banner.getStatus());

        banner.setTitle(trimToNull(request.getTitle()));
        banner.setLinkUrl(trimToNull(request.getLinkUrl()));
        banner.setPositionCode(newPositionCode);
        banner.setStatus(newStatus);
        replaceImages(banner, request.getImages());
        if (newStatus == ACTIVE_STATUS) {
            deactivateOtherActiveBanners(newPositionCode, banner.getId());
        }

        return toResponse(bannerRepository.save(banner));
    }

    @Override
    @Transactional
    public BannerResponse updateStatus(Long id, Integer status) {
        Banner banner = getExistingBanner(id);
        banner.setStatus(status);
        if (status != null && status == ACTIVE_STATUS) {
            deactivateOtherActiveBanners(banner.getPositionCode(), banner.getId());
        }
        return toResponse(bannerRepository.save(banner));
    }

    @Override
    @Transactional
    public BannerResponse restore(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Banner not found"));

        if (!Boolean.TRUE.equals(banner.getIsDeleted())) {
            throw new BusinessException("Banner is not in trash");
        }

        banner.setIsDeleted(false);
        banner.getImages().forEach(image -> image.setIsDeleted(false));
        if (banner.getStatus() != null && banner.getStatus() == ACTIVE_STATUS) {
            deactivateOtherActiveBanners(banner.getPositionCode(), banner.getId());
        }
        return toResponse(bannerRepository.save(banner));
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        Banner banner = getExistingBanner(id);
        banner.setIsDeleted(true);
        banner.getImages().forEach(image -> image.setIsDeleted(true));
        bannerRepository.save(banner);
    }

    @Override
    @Transactional
    public void hardDelete(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Banner not found"));
        bannerRepository.delete(banner);
    }

    @Override
    @Transactional(readOnly = true)
    public BannerResponse getAdminById(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Banner not found"));
        return toResponse(banner);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BannerResponse> getAdminBanners(int page, int pageSize) {
        Pageable pageable = buildPageable(page, pageSize);
        return bannerRepository.findByIsDeletedFalse(pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BannerResponse> getTrash(int page, int pageSize) {
        Pageable pageable = buildPageable(page, pageSize);
        return bannerRepository.findByIsDeletedTrue(pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BannerResponse getPublicBanner(BannerPosition positionCode) {
        List<Banner> banners = bannerRepository.findActiveBannersByPosition(positionCode);
        if (banners.isEmpty()) {
            return null;
        }
        return toResponse(banners.get(0));
    }

    private Banner getExistingBanner(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Banner not found"));

        if (Boolean.TRUE.equals(banner.getIsDeleted())) {
            throw new BusinessException("Banner is in trash");
        }

        return banner;
    }

    private int defaultCreateStatus(Integer status) {
        return status == null ? INACTIVE_STATUS : status;
    }

    private int resolveUpdatedStatus(Integer requestedStatus, Integer currentStatus) {
        return requestedStatus == null ? (currentStatus == null ? INACTIVE_STATUS : currentStatus) : requestedStatus;
    }

    private BannerPosition defaultPositionCode(BannerPosition positionCode) {
        return positionCode == null ? DEFAULT_POSITION_CODE : positionCode;
    }

    private void deactivateOtherActiveBanners(BannerPosition positionCode, Long excludedBannerId) {
        List<Banner> activeBanners = bannerRepository.findByPositionAndStatusAndIsDeletedFalse(positionCode, ACTIVE_STATUS);
        for (Banner activeBanner : activeBanners) {
            if (excludedBannerId != null && excludedBannerId.equals(activeBanner.getId())) {
                continue;
            }
            activeBanner.setStatus(INACTIVE_STATUS);
        }
        bannerRepository.saveAll(activeBanners);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private Pageable buildPageable(int page, int pageSize) {
        return PageRequest.of(Math.max(page, 0), pageSize, Sort.by("position").ascending().and(Sort.by("id").descending()));
    }

    private void replaceImages(Banner banner, List<BannerImageRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new BusinessException("Banner must contain at least one image");
        }

        List<BannerImageRequest> normalizedRequests = normalizeImageRequests(requests);

        banner.getImages().clear();
        for (BannerImageRequest request : normalizedRequests) {
            BannerImage image = new BannerImage();
            image.setImageUrl(request.getImageUrl().trim());
            image.setDisplayOrder(request.getDisplayOrder());
            image.setDurationMs(defaultDuration(request.getDurationMs()));
            image.setStartTime(request.getStartTime());
            image.setEndTime(request.getEndTime());
            image.setIsDeleted(false);
            if (image.getStartTime() != null && image.getEndTime() != null
                    && image.getEndTime().isBefore(image.getStartTime())) {
                throw new BusinessException("Image end time must be after start time");
            }
            banner.addImage(image);
        }
    }

    private List<BannerImageRequest> normalizeImageRequests(List<BannerImageRequest> requests) {
        List<BannerImageRequest> normalized = new ArrayList<>(requests);

        for (int index = 0; index < normalized.size(); index++) {
            normalized.get(index).setDisplayOrder(index);
        }
        return normalized;
    }

    private int defaultDuration(Integer durationMs) {
        return durationMs == null || durationMs <= 0 ? DEFAULT_IMAGE_DURATION_MS : durationMs;
    }

    private BannerResponse toResponse(Banner banner) {
        BannerResponse response = new BannerResponse();
        response.setId(banner.getId());
        response.setTitle(banner.getTitle());
        response.setLinkUrl(banner.getLinkUrl());
        response.setPositionCode(banner.getPositionCode());
        response.setStatus(banner.getStatus());
        response.setDeleted(Boolean.TRUE.equals(banner.getIsDeleted()));
        response.setImages(banner.getImages().stream()
                .sorted(Comparator.comparing(BannerImage::getDisplayOrder))
                .map(this::toImageResponse)
                .toList());
        response.setCreatedAt(banner.getCreatedAt());
        response.setUpdatedAt(banner.getUpdatedAt());
        return response;
    }

    private BannerImageResponse toImageResponse(BannerImage image) {
        BannerImageResponse response = new BannerImageResponse();
        response.setId(image.getId());
        response.setImageUrl(image.getImageUrl());
        response.setDisplayOrder(image.getDisplayOrder());
        response.setDurationMs(image.getDurationMs());
        response.setStartTime(image.getStartTime());
        response.setEndTime(image.getEndTime());
        response.setDeleted(Boolean.TRUE.equals(image.getIsDeleted()));
        response.setCreatedAt(image.getCreatedAt());
        response.setUpdatedAt(image.getUpdatedAt());
        return response;
    }
}
