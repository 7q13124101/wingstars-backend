package com.wingstars.cheerleader.repository;

import com.wingstars.cheerleader.entity.Cheerleader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CheerleaderRepository extends JpaRepository<Cheerleader, Long> {
    boolean existsByFullName(String fullName);

    Page<Cheerleader> findByFullNameContainingIgnoreCaseAndIsDeletedFalse(String fullName, Pageable pageable);
    Page<Cheerleader> findByIsDeletedFalse(Pageable pageable);
    Optional<Cheerleader> findByIdAndIsDeletedFalse(Long id);
}
