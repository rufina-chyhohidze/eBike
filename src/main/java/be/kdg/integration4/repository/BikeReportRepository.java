package be.kdg.integration4.repository;

import be.kdg.integration4.domain.BikeReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BikeReportRepository extends JpaRepository<BikeReport, Long> {
    @Query("SELECT br FROM BikeReport br " +
            "LEFT JOIN FETCH br.bike " +
            "LEFT JOIN FETCH br.customer " +
            "LEFT JOIN FETCH br.technician " +
            "LEFT JOIN FETCH br.testBench")
    List<BikeReport> findAllWithDetails();

//    @Query("SELECT br FROM BikeReport br " +
//            "JOIN br.bike b " +
//            "JOIN br.customer c " +
//            "WHERE (:frameNumber IS NULL OR b.frameNumber LIKE %:frameNumber%) " +
//            "AND (:customerName IS NULL OR c.name LIKE %:customerName%)")
//    List<BikeReport> searchReports(@Param("frameNumber") String frameNumber,
//                                   @Param("customerName") String customerName);
}
