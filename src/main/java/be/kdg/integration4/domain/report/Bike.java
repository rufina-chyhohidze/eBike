package be.kdg.integration4.domain.report;

import be.kdg.integration4.domain.enums.BikeSize;
import be.kdg.integration4.domain.profile.Customer;
import jakarta.persistence.*;
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

    private LocalDateTime registrationDate;

    private LocalDate productionDate;

    private int milleage;

    private int accCapacity;

    @ManyToOne
    private BikeModel bikeModel;

    public Bike(String frameNumber, Customer bikeOwner, int milleage, int accCapacity, LocalDate productionDate,  BikeModel bikeModel) {
        this.frameNumber = frameNumber;
        this.bikeOwner = bikeOwner;
        this.milleage = milleage;
        this.accCapacity = accCapacity;
        this.productionDate = productionDate;
        this.bikeModel = bikeModel;
        this.registrationDate = LocalDateTime.now();
    }

    public Bike(String frameNumber, Customer bikeOwner, String type, String brand, LocalDate productionDate, BikeSize bikeSize, int milleage, String gearType, String engineType, String powertrain, int accCapacity, double maxSupport, int enginePowerMax, int enginePowerNominal, int engineTorque) {
        this.frameNumber = frameNumber;
        this.bikeOwner = bikeOwner;
        BikeModel bikeModel = new BikeModel();
        bikeModel.setType(type);
        bikeModel.setBrand(brand);
        this.registrationDate = LocalDateTime.now();
        this.productionDate = productionDate;
        bikeModel.setBikeSize(bikeSize);
        this.milleage = milleage;
        bikeModel.setGearType(gearType);
        bikeModel.setEngineType(engineType);
        bikeModel.setPowertrain(powertrain);
        this.accCapacity = accCapacity;
        bikeModel.setMaxSupport(maxSupport);
        bikeModel.setEnginePowerMax(enginePowerMax);
        bikeModel.setEnginePowerNominal(enginePowerNominal);
        bikeModel.setEngineTorque(engineTorque);
        this.bikeModel = bikeModel;
    }

    @Override
    public int compareTo(Bike bike) {
        return bike.getRegistrationDate().compareTo(this.registrationDate);
    }
}
