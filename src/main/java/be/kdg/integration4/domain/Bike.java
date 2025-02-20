package be.kdg.integration4.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Bike {
    @Id
    private String frameNumber;

    private String type;

    private String brand;

    private LocalDate registrationDate;

    private LocalDate productionDate;

    private BikeSize bikeSize;

    private int milleage;

    private String gearType;

    private String engineType;

    private String powertrain;

    private int accCapacity;

    private double maxSupport;

    private int enginePowerMax;

    private int enginePowerNominal;

    private int engineTorque;
}
