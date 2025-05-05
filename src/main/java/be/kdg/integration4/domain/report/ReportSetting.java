package be.kdg.integration4.domain.report;

import be.kdg.integration4.domain.profile.Technician;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ReportSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Technician technician; // Link to the technician

    private double horizontalVibration;
    private double verticalVibration;


    public ReportSetting(Technician technician) {
        this.technician = technician;
    }

    public ReportSetting(Technician technician, double horizontalVibration, double verticalVibration) {
        this.technician = technician;
        this.horizontalVibration = horizontalVibration;
        this.verticalVibration = verticalVibration;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Technician getTechnician() {
        return technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }

    public double getHorizontalVibration() {
        return horizontalVibration;
    }

    public void setHorizontalVibration(double horizontalVibration) {
        this.horizontalVibration = horizontalVibration;
    }

    public double getVerticalVibration() {
        return verticalVibration;
    }

    public void setVerticalVibration(double verticalVibration) {
        this.verticalVibration = verticalVibration;
    }
}
