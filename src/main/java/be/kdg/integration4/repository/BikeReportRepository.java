package be.kdg.integration4.repository;

import be.kdg.integration4.domain.report.BikeReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

@Repository
public interface BikeReportRepository extends JpaRepository<BikeReport, Long> {
    @Query("SELECT br FROM BikeReport br " +
            "LEFT JOIN FETCH br.bike " +
            "LEFT JOIN FETCH br.customer " +
            "LEFT JOIN FETCH br.technician " +
            "LEFT JOIN FETCH br.testBench")
    List<BikeReport> findAllWithDetails();

    @Query("SELECT br FROM BikeReport br " +
            "LEFT JOIN FETCH br.bike b " +
            "LEFT JOIN FETCH br.customer " +
            "LEFT JOIN FETCH br.technician " +
            "LEFT JOIN FETCH br.testBench " +
            "WHERE b.frameNumber = :frameNumber")
    List<BikeReport> findBikeReportsByBikeFrameNumberWithDetails(String frameNumber);


    //    @Query("SELECT br FROM BikeReport br " +
//            "JOIN br.bike b " +
//            "JOIN br.customer c " +
//            "WHERE (:frameNumber IS NULL OR b.frameNumber LIKE %:frameNumber%) " +
//            "AND (:customerName IS NULL OR c.name LIKE %:customerName%)")
//    List<BikeReport> searchReports(@Param("frameNumber") String frameNumber,
//                                   @Param("customerName") String customerName);
    int countByTechnicianId(Long technicianId);

    @Query("SELECT br FROM BikeReport br LEFT JOIN FETCH br.testLines WHERE br.id = :id")
    BikeReport findByIdWithTestLines(Long id);

    @Query("""
            SELECT br
            FROM BikeReport br
            LEFT JOIN FETCH br.testLines
            LEFT JOIN FETCH br.bike
            LEFT JOIN FETCH br.visualInspection
            LEFT JOIN FETCH br.functionalTest
            WHERE br.id = :id
            """)
    BikeReport findByIdWithTestLinesAndBikeAndVisualInspectionAndFunctionalTest(@Param("id") Long id);

    List<BikeReport> getBikeReportByCustomerId(Long customerId);

    BikeReport findByTestId(String testId);

    @Query("""
                SELECT br FROM BikeReport br
                LEFT JOIN FETCH br.bike b
                LEFT JOIN FETCH b.bikeModel m
                LEFT JOIN FETCH br.technician t
                LEFT JOIN FETCH br.customer c
                LEFT JOIN FETCH br.testBench tb
                LEFT JOIN FETCH br.visualInspection
                LEFT JOIN FETCH br.functionalTest
                WHERE br.id = :id
            """
    )
    Optional<BikeReport> findByIdWithCustomer(Long id);

    @Query("""
                SELECT br FROM BikeReport br
                LEFT JOIN FETCH br.bike b
                LEFT JOIN FETCH b.bikeModel m
                LEFT JOIN FETCH br.technician t
                LEFT JOIN FETCH br.customer c
                LEFT JOIN FETCH br.testBench tb
                WHERE t.id = :techId
            """)
    List<BikeReport> findAllByTechnicianWithDetails(@Param("techId") Long techId);

    @Query(
            """
                    SELECT br FROM BikeReport br
                    LEFT JOIN FETCH br.technician t
                    LEFT JOIN FETCH t.workshop ws
                    WHERE ws.workshopId = :workshopId
                    """
    )
    List<BikeReport> findBikeReportByWorkshopId(Long workshopId);

    @Query("""
        SELECT br FROM BikeReport br
        LEFT JOIN FETCH br.bike b
        LEFT JOIN FETCH b.bikeModel m
        LEFT JOIN FETCH br.technician t
        LEFT JOIN FETCH br.customer c
        LEFT JOIN FETCH br.testBench tb
        """)
    List<BikeReport> findAll();
}
