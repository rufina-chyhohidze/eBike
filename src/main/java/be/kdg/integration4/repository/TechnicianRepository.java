package be.kdg.integration4.repository;

import be.kdg.integration4.domain.profile.Technician;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Long> {
    Optional<Technician> findByEmail(String email);
}
