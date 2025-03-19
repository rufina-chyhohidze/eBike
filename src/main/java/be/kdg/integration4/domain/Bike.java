package be.kdg.integration4.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Bike {
    @Id
    private String frameNumber;

    @ManyToOne
    @JoinColumn(name = "bike_owner_id", nullable = false)
    private Customer bikeOwner;

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

    public Bike(String frameNumber, String type, String brand, LocalDate registrationDate, LocalDate productionDate, BikeSize bikeSize, int milleage, String gearType, String engineType, String powertrain, int accCapacity, double maxSupport, int enginePowerMax, int enginePowerNominal, int engineTorque) {
        this.frameNumber = frameNumber;
        this.type = type;
        this.brand = brand;
        this.registrationDate = registrationDate;
        this.productionDate = productionDate;
        this.bikeSize = bikeSize;
        this.milleage = milleage;
        this.gearType = gearType;
        this.engineType = engineType;
        this.powertrain = powertrain;
        this.accCapacity = accCapacity;
        this.maxSupport = maxSupport;
        this.enginePowerMax = enginePowerMax;
        this.enginePowerNominal = enginePowerNominal;
        this.engineTorque = engineTorque;
    }//branch
}
