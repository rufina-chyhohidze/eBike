package be.kdg.integration4.repository;

import be.kdg.integration4.domain.BikeReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BikeReportRepository extends JpaRepository<BikeReport, Long> {
    int countByTechnicianId(Long technicianId);
}
