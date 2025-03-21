package be.kdg.integration4.domain.report;

import be.kdg.integration4.domain.enums.BikeSize;
import be.kdg.integration4.domain.profile.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Bike implements Comparable<Bike> {

    @Id
    private String frameNumber;

    @ManyToOne
    @JoinColumn(name = "bike_owner_id", nullable = false)
    private Customer bikeOwner;

    private String type;

    private String brand;

    private LocalDateTime registrationDate;

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

    public Bike(String frameNumber, Customer bikeOwner, String type, String brand, LocalDateTime registrationDate, LocalDate productionDate, BikeSize bikeSize, int milleage, String gearType, String engineType, String powertrain, int accCapacity, double maxSupport, int enginePowerMax, int enginePowerNominal, int engineTorque) {
        this.frameNumber = frameNumber;
        this.bikeOwner = bikeOwner;
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
    }

    @Override
    public int compareTo(Bike bike) {
        return bike.getRegistrationDate().compareTo(this.registrationDate);
    }
}
