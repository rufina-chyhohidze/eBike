package be.kdg.integration4.repository;

import be.kdg.integration4.domain.ApiRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApiRequestRepository extends JpaRepository<ApiRequest, Long> {
    void deleteApiRequestByTestId(String testId);

    @Query("""
    SELECT api FROM ApiRequest api
    LEFT JOIN FETCH api.report r
    LEFT JOIN FETCH r.bike b
    LEFT JOIN FETCH r.technician t
    LEFT JOIN FETCH r.customer c
    LEFT JOIN FETCH r.testBench tb
    WHERE api.testId = :testId
    """)
    Optional<ApiRequest> findApiRequestByTestId(@Param("testId") String testId);

}
