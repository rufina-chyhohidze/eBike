package be.kdg.integration4.repository;

import be.kdg.integration4.domain.BikeReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikeReportRepository extends JpaRepository<BikeReport, Long> {
}

