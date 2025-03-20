package be.kdg.integration4.domain.report;

import be.kdg.integration4.domain.enums.Location;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Workshop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workshopId;
    private String workshopName;
    @Enumerated(EnumType.STRING)
    private Location workshopLocation;

    public Workshop(String workshopName, Location workshopLocation) {
        this.workshopName = workshopName;
        this.workshopLocation = workshopLocation;
    }
}
