package be.kdg.integration4.controller.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDto {
    private int testbenchNumber;
    private String testType;
    private String emailBikeOwner;
    private String chassisNumber;
    private String bikeBrand;
    private String bikeSize;
    private int productionDate;
    private String type;
    private String powertrain;
    private int enginePowerMax;
    private int mileage;
    private int accuCapacity;
    private int enginePowerNominal;
    private String gearType;
    private int maxSupport;
    private int engineTorque;
    private String engineType;
}