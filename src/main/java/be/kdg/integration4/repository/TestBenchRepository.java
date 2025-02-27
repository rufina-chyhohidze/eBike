package be.kdg.integration4.repository;

import be.kdg.integration4.domain.TestBench;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestBenchRepository extends JpaRepository<TestBench, Long> {
}
