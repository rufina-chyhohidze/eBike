package be.kdg.integration4.repository;

import be.kdg.integration4.domain.report.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {
}
