package be.kdg.integration4.repository;

import be.kdg.integration4.domain.report.TestLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestLineRepository extends JpaRepository<TestLine, Long> {

    @Query(value = "SELECT * FROM test_line WHERE bikereport_id = :reportId", nativeQuery = true)
    List<TestLine> findByBikeReportId(Long reportId);

}
