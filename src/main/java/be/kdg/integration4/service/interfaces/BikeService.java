package be.kdg.integration4.service.interfaces;

import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.domain.enums.BikeSize;
import be.kdg.integration4.domain.report.BikeModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BikeService {
    Optional<Bike> getByFrameNumber(String frameNumber);
    List<Bike> getAll();

    Bike save(String frameNumber, Long bikeOwnerID, String type, String brand, LocalDate productionDate, BikeSize bikeSize, int milleage, String gearType, String engineType, String powertrain, int accCapacity, double maxSupport, int enginePowerMax, int enginePowerNominal, int engineTorque, Long bikeModelId);

    void delete(String frameNumber);

    List<BikeModel> getAllBikeModels();
    List<Bike> getAllByOwnerId(Long ownerId);

    void unlinkBikeFromCustomer(String frameNumber, Long customerId);

    List<Bike> getAllAvailableForUserByFrameNumber(Long customerId, String frameNumber);
}
