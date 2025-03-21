package be.kdg.integration4.repository;

import be.kdg.integration4.domain.report.TestBench;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestBenchRepository extends JpaRepository<TestBench, Long> {
}
