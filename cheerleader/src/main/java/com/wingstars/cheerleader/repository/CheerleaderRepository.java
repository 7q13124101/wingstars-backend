package com.wingstars.cheerleader.repository;

import com.wingstars.cheerleader.entity.Cheerleader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheerleaderRepository extends JpaRepository<Cheerleader, Long> {
    Optional<Cheerleader> findByIdAndIsDeletedFalse(Long id);

    Page<Cheerleader> findByIsDeletedFalse(Pageable pageable);

    Page<Cheerleader> findByFullNameContainingIgnoreCaseAndIsDeletedFalse(String fullName, Pageable pageable);
}
