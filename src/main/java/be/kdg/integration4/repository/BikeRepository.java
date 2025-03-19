package be.kdg.integration4.repository;

import be.kdg.integration4.domain.Bike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BikeRepository extends JpaRepository<Bike, String> {
    Optional<Bike> findBikeByFrameNumber(String frameNumber);
    Set<Bike> findBikesByBikeOwnerId(Long bikeOwnerId);
}
