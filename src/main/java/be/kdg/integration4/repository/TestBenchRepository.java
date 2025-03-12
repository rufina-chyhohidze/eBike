package be.kdg.integration4.repository;

import be.kdg.integration4.domain.TestBench;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestBenchRepository extends JpaRepository<TestBench, Long> {
}
