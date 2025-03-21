package be.kdg.integration4.repository;

import be.kdg.integration4.domain.profile.SystemAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemAdminRepository extends JpaRepository<SystemAdmin, Long> {
}
