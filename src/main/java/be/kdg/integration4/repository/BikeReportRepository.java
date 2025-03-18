package be.kdg.integration4.repository;

import be.kdg.integration4.domain.BikeReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BikeReportRepository extends JpaRepository<BikeReport, Long> {
    List<BikeReport> getBikeReportByCustomerId(Long customerId);
}
