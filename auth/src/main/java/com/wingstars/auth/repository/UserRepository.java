package com.wingstars.auth.repository;

import com.wingstars.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.username = :username AND u.deleted = false")
    Optional<User> findByUsernameWithRole(@Param("username") String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
