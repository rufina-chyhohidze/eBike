package be.kdg.integration4.repository;

import be.kdg.integration4.domain.Bike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikeRepository extends JpaRepository<Bike, String> {
}
