package be.kdg.integration4.repository;

import be.kdg.integration4.domain.Bike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BikeRepository extends JpaRepository<Bike, String> {
    Optional<Bike> findBikeByFrameNumber(String frameNumber);
}
