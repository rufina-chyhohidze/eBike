package be.kdg.integration4.repository;

import be.kdg.integration4.domain.report.Bike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BikeRepository extends JpaRepository<Bike, String> {

    @Query("""
        SELECT b FROM Bike b
        LEFT JOIN FETCH b.bikeModel bm
        WHERE b.frameNumber = :framenumber
        """)
    Optional<Bike> findBikeByFrameNumberWithBikeModel(String framenumber);

    @Query("""
        SELECT b FROM Bike b 
        LEFT JOIN FETCH b.bikeModel bm
        WHERE b.bikeOwner.id = :bikeOwnerId
        """)
    Set<Bike> findBikesWithBikeModelByBikeOwnerId(Long bikeOwnerId);
}
