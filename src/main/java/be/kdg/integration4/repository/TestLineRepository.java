package be.kdg.integration4.repository;

import be.kdg.integration4.domain.report.TestLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestLineRepository extends JpaRepository<TestLine, Long> {
}
