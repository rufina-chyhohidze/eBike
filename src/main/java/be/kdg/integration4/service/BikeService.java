package be.kdg.integration4.service;

import be.kdg.integration4.domain.Bike;
import be.kdg.integration4.domain.BikeSize;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BikeService {
    Optional<Bike> findByFrameNumber(String frameNumber);
    List<Bike> findAll();
    void save(String frameNumber, String type, String brand, LocalDate registrationDate, LocalDate productionDate, BikeSize bikeSize, int milleage, String gearType, String engineType, String powertrain, int accCapacity, double maxSupport, int enginePowerMax, int enginePowerNominal, int engineTorque);
    void delete(String frameNumber);
}
