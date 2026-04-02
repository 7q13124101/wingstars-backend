package wingstars.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wingstars.backend.entities.RefreshToken;
import wingstars.backend.entities.User;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByRefreshTokenCode(String refreshToken);

    List<RefreshToken> findAllByUser(User user);
}
