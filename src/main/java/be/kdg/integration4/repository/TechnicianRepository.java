package be.kdg.integration4.repository;

import be.kdg.integration4.domain.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.lang.annotation.Native;

public interface TechnicianRepository extends JpaRepository<Technician, Long> {
    Technician findByEmail(String email);
}
