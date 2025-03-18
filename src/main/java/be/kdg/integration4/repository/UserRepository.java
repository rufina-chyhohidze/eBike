package be.kdg.integration4.repository;

import be.kdg.integration4.domain.Technician;
import be.kdg.integration4.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<Technician> findTechnicianById(@Param("id") Long id);
}
