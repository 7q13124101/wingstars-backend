package wingstars.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import wingstars.backend.entities.Role;
import wingstars.backend.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByUserName(String UserName);

    User findByEmail(String email);

    List<User> findByRole(Role role);
}
