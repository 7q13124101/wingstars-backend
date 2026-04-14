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

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        banner.setPositionCode(defaultPositionCode(request.getPositionCode()));
        banner.setStatus(defaultCreateStatus(request.getStatus()));
        banner.setIsDeleted(false);
        banner.setDurationMs(defaultDuration(request.getDurationMs()));
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
        banner.setPositionCode(newPositionCode);
        banner.setStatus(newStatus);
        banner.setDurationMs(defaultDuration(request.getDurationMs()));
        replaceImages(banner, request.getImages());
        if (newStatus == ACTIVE_STATUS) {
            deactivateOtherActiveBanners(newPositionCode, banner.getId());
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
        if (banner.getStatus() != null && banner.getStatus() == ACTIVE_STATUS) {
            deactivateOtherActiveBanners(banner.getPositionCode(), banner.getId());
        }
        return toResponse(bannerRepository.save(banner));
    }

    @Override
    @Transactional
    public BannerResponse updateStatus(Long id, Integer status) {
        Banner banner = getExistingBanner(id);
        int newStatus = status != null ? status : (banner.getStatus() != null ? banner.getStatus() : INACTIVE_STATUS);
        banner.setStatus(newStatus);
        if (newStatus == ACTIVE_STATUS) {
            deactivateOtherActiveBanners(banner.getPositionCode(), banner.getId());
        }
        return toResponse(bannerRepository.save(banner));
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        Banner banner = getExistingBanner(id);
        banner.setIsDeleted(true);
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

    private Banner getExistingBanner(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Banner not found"));

        if (Boolean.TRUE.equals(banner.getIsDeleted())) {
            throw new BusinessException("Banner is in trash");
        }

        return banner;
    }

    private BannerPosition defaultPositionCode(BannerPosition positionCode) {
        return positionCode == null ? DEFAULT_POSITION_CODE : positionCode;
    }

    private int defaultCreateStatus(Integer status) {
        return status == null ? INACTIVE_STATUS : status;
    }

    private int resolveUpdatedStatus(Integer requestedStatus, Integer currentStatus) {
        if (requestedStatus != null) {
            return requestedStatus;
        }
        return currentStatus != null ? currentStatus : INACTIVE_STATUS;
    }

    private void deactivateOtherActiveBanners(BannerPosition position, Long excludeId) {
        List<Banner> activeBanners = bannerRepository.findByPositionAndStatusAndIsDeletedFalse(position, ACTIVE_STATUS);
        for (Banner b : activeBanners) {
            if (excludeId == null || !excludeId.equals(b.getId())) {
                b.setStatus(INACTIVE_STATUS);
                bannerRepository.save(b);
            }
        }
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
            image.setLinkUrl(trimToNull(request.getLinkUrl()));
            image.setDisplayOrder(request.getDisplayOrder());
            image.setStartTime(request.getStartTime());
            image.setEndTime(request.getEndTime());
            image.setIsDeleted(toDeletedFlag(request.getStatus()));
            if (image.getStartTime() != null && image.getEndTime() != null
                    && image.getEndTime().isBefore(image.getStartTime())) {
                throw new BusinessException("Image end time must be after start time");
            }
            banner.addImage(image);
        }
    }

    private List<BannerImageRequest> normalizeImageRequests(List<BannerImageRequest> requests) {
        List<BannerImageRequest> normalized = new ArrayList<>(requests);
        boolean hasMissingDisplayOrder = normalized.stream().anyMatch(request -> request.getDisplayOrder() == null);

        if (hasMissingDisplayOrder) {
            for (int index = 0; index < normalized.size(); index++) {
                if (normalized.get(index).getDisplayOrder() == null) {
                    normalized.get(index).setDisplayOrder(index);
                }
            }
        }

        long distinctDisplayOrderCount = normalized.stream()
                .map(BannerImageRequest::getDisplayOrder)
                .filter(Objects::nonNull)
                .distinct()
                .count();
        if (distinctDisplayOrderCount != normalized.size()) {
            throw new BusinessException("Image display order must be unique inside banner");
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
        response.setPositionCode(banner.getPositionCode());
        response.setStatus(banner.getStatus());
        response.setDurationMs(banner.getDurationMs());
        response.setDisplayOrder(banner.getDisplayOrder());
        response.setDeleted(Boolean.TRUE.equals(banner.getIsDeleted()));
        response.setImages(banner.getImages().stream()
                .sorted(Comparator.comparing(BannerImage::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)))
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
        response.setLinkUrl(image.getLinkUrl());
        response.setDisplayOrder(image.getDisplayOrder());
        response.setStartTime(image.getStartTime());
        response.setEndTime(image.getEndTime());
        response.setStatus(toStatus(image.getIsDeleted()));
        response.setDeleted(Boolean.TRUE.equals(image.getIsDeleted()));
        response.setCreatedAt(image.getCreatedAt());
        response.setUpdatedAt(image.getUpdatedAt());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public BannerResponse getPublicBanner(BannerPosition positionCode) {
        List<Banner> banners = bannerRepository.findActiveBannersByPosition(positionCode);
        if (banners.isEmpty()) {
            return null;
        }

        BannerResponse response = toResponse(banners.get(0));
        response.setImages(response.getImages().stream()
                .filter(this::isPublicImageVisible)
                .sorted(Comparator.comparing(BannerImageResponse::getDisplayOrder, Comparator.nullsLast(Integer::compareTo)))
                .toList());
        return response.getImages().isEmpty() ? null : response;
    }

    private boolean isPublicImageVisible(BannerImageResponse image) {
        LocalDateTime now = LocalDateTime.now();
        boolean activeStatus = image.getStatus() != null && image.getStatus() == 1;
        boolean inStartWindow = image.getStartTime() == null || !image.getStartTime().isAfter(now);
        boolean inEndWindow = image.getEndTime() == null || !image.getEndTime().isBefore(now);
        return activeStatus && inStartWindow && inEndWindow;
    }

    private Boolean toDeletedFlag(Integer status) {
        return status != null && status == 0;
    }

    private Integer toStatus(Boolean isDeleted) {
        return Boolean.TRUE.equals(isDeleted) ? 0 : 1;
    }
}
