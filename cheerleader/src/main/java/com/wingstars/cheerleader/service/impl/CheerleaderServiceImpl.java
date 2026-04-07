package com.wingstars.cheerleader.service.impl;

import com.wingstars.cheerleader.dto.request.CheerleaderRequest;
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
    public Page<Cheerleader> getAll(String search, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        if (StringUtils.hasText(search)) {
            return cheerleaderRepository.findByFullNameContainingIgnoreCaseAndIsDeletedFalse(search.trim(), pageable);
        }

        return cheerleaderRepository.findByIsDeletedFalse(pageable);
    }

    @Override
    @Transactional
    public Cheerleader create(CheerleaderRequest request) {
        Cheerleader cheerleader = modelMapper.map(request, Cheerleader.class);
        cheerleader.setIsDeleted(false);
        return cheerleaderRepository.save(cheerleader);
    }

    @Override
    @Transactional
    public Cheerleader update(Long id, CheerleaderRequest request) {
        Cheerleader cheerleader = cheerleaderRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Cheerleader does not exist or has been deleted"));

        modelMapper.map(request, cheerleader);
        return cheerleaderRepository.save(cheerleader);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Cheerleader cheerleader = cheerleaderRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Cheerleader does not exist or has been deleted"));

        cheerleader.setIsDeleted(true);
        cheerleaderRepository.save(cheerleader);
    }
}
