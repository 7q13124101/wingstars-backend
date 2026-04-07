package com.wingstars.cheerleader.service.impl;

import com.wingstars.cheerleader.dto.request.CheerleaderRequest;
import com.wingstars.cheerleader.dto.response.CheerleaderResponse;
import com.wingstars.cheerleader.entity.Cheerleader;
import com.wingstars.cheerleader.repository.CheerleaderRepository;
import com.wingstars.cheerleader.service.CheerleaderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CheerleaderServiceImpl implements CheerleaderService {
    private final CheerleaderRepository cheerleaderRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CheerleaderResponse> getAll(String search, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        if (StringUtils.hasText(search)) {
            return cheerleaderRepository.findByFullNameContainingIgnoreCaseAndIsDeletedFalse(search.trim(), pageable)
                    .map(this::toResponse);
        }

        return cheerleaderRepository.findByIsDeletedFalse(pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CheerleaderResponse getById(Long id) {
        Cheerleader idol = cheerleaderRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Cheerleader does not exist or has been deleted"));
        return toResponse(idol);
    }

    @Override
    @Transactional
    public CheerleaderResponse create(CheerleaderRequest request) {
        Cheerleader cheerleader = modelMapper.map(request, Cheerleader.class);
        cheerleader.setIsDeleted(false);
        return toResponse(cheerleaderRepository.save(cheerleader));
    }

    @Override
    @Transactional
    public CheerleaderResponse update(Long id, CheerleaderRequest request) {
        Cheerleader existingIdol = cheerleaderRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Cheerleader does not exist or has been deleted"));

        String oldAvatarUrl = existingIdol.getAvatarUrl();
        String oldAudioUrl = existingIdol.getExclusiveAudioUrl();
        String oldFrameUrl = existingIdol.getPhotoFrameUrl();

        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(request, existingIdol);

        if (!StringUtils.hasText(request.getAvatarUrl())) {
            existingIdol.setAvatarUrl(oldAvatarUrl);
        }
        if (!StringUtils.hasText(request.getExclusiveAudioUrl())) {
            existingIdol.setExclusiveAudioUrl(oldAudioUrl);
        }
        if (!StringUtils.hasText(request.getPhotoFrameUrl())) {
            existingIdol.setPhotoFrameUrl(oldFrameUrl);
        }

        Cheerleader updatedIdol = cheerleaderRepository.save(existingIdol);
        return toResponse(updatedIdol);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Cheerleader cheerleader = cheerleaderRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Cheerleader does not exist or has been deleted"));

        cheerleader.setIsDeleted(true);
        cheerleaderRepository.save(cheerleader);
    }

    private CheerleaderResponse toResponse(Cheerleader cheerleader) {
        return modelMapper.map(cheerleader, CheerleaderResponse.class);
    }
}
