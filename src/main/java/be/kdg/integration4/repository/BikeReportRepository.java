package be.kdg.integration4.repository;

import be.kdg.integration4.domain.BikeReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BikeReportRepository extends JpaRepository<BikeReport, Long> {
    @Query("SELECT br FROM BikeReport br LEFT JOIN FETCH br.testLines WHERE br.id = :id")
    BikeReport findByIdWithTestLines(Long id);
}