package be.kdg.integration4.repository;

import be.kdg.integration4.domain.WorkshopAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkshopAdminRepository extends JpaRepository<WorkshopAdmin, Long> {
}
