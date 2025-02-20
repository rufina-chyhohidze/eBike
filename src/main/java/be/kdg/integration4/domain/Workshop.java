package be.kdg.integration4.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Workshop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workshopId;
    private String workshopName;
    private Location workshopLocation;

    public Workshop(String workshopName, Location workshopLocation) {
        this.workshopName = workshopName;
        this.workshopLocation = workshopLocation;
    }
}
