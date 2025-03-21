package be.kdg.integration4.repository;

import be.kdg.integration4.domain.profile.Technician;
import be.kdg.integration4.domain.profile.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<Technician> findTechnicianById(@Param("id") Long id);
    @Query("SELECT u FROM User u WHERE u.approved = false AND TYPE(u) IN (be.kdg.integration4.domain.profile.WorkshopAdmin, be.kdg.integration4.domain.profile.Technician)")
    List<User> findUnapprovedUsers();
}
