package be.kdg.integration4.repository;

import be.kdg.integration4.domain.report.BikeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikeModelRepository extends JpaRepository<BikeModel,Long> {
}
