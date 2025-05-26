package be.kdg.integration4.repository;

import be.kdg.integration4.domain.profile.Technician;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Long> {
    Technician findByEmail(String email);

    @Query("""
    SELECT t FROM Technician t
    JOIN FETCH t.workshop ws
    WHERE ws.workshopId = :workshopId
""")
    List<Technician> findTechnicianByWorkshopId(Long workshopId);
}
