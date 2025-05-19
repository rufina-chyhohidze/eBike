package be.kdg.integration4.domain.report;

import be.kdg.integration4.domain.enums.BikeSize;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class BikeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String brand;


    private String gearType;

    private String engineType;

    private String powertrain;

    private double maxSupport;

    private int enginePowerMax;

    private int enginePowerNominal;

    private int engineTorque;

}
