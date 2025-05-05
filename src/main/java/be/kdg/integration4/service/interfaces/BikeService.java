package be.kdg.integration4.service.interfaces;

import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.domain.enums.BikeSize;
import be.kdg.integration4.domain.report.BikeModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BikeService {
    Optional<Bike> getByFrameNumber(String frameNumber);
    List<Bike> getAll();
    void save(String frameNumber, Long bikeOwnerID, String type, String brand, LocalDateTime registrationDate, LocalDate productionDate, BikeSize bikeSize, int milleage, String gearType, String engineType, String powertrain, int accCapacity, double maxSupport, int enginePowerMax, int enginePowerNominal, int engineTorque);
    void delete(String frameNumber);

    List<BikeModel> getAllBikeModels();
    Set<Bike> getAllByOwnerId(Long ownerId);
}
