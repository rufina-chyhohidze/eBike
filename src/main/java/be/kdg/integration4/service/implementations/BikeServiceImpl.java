package be.kdg.integration4.service.implementations;

import be.kdg.integration4.domain.report.Bike;
import be.kdg.integration4.domain.enums.BikeSize;
import be.kdg.integration4.domain.report.BikeModel;
import be.kdg.integration4.repository.BikeModelRepository;
import be.kdg.integration4.repository.BikeRepository;
import be.kdg.integration4.repository.CustomerRepository;
import be.kdg.integration4.service.interfaces.BikeService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BikeServiceImpl implements BikeService {
    private final BikeRepository bikeRepository;
    private final BikeModelRepository bikeModelRepository;
    private final CustomerRepository customerRepository;

    public BikeServiceImpl(BikeRepository bikeRepository, BikeModelRepository bikeModelRepository, CustomerRepository customerRepository) {
        this.bikeRepository = bikeRepository;
        this.bikeModelRepository = bikeModelRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Bike> getByFrameNumber(String frameNumber) {
         return bikeRepository.findBikeByFrameNumberWithBikeModel(frameNumber);
    }

    @Override
    public Set<Bike> getAllByOwnerId(Long ownerId) {
        return this.bikeRepository.findBikesWithBikeModelByBikeOwnerId(ownerId);
    }

    @Override
    public List<Bike> getAll() {
        return bikeRepository.findAll();
    }

    @Override
    public Bike save(String frameNumber, Long bikeOwnerID, String type, String brand, LocalDate productionDate, BikeSize bikeSize, int milleage, String gearType, String engineType, String powertrain, int accCapacity, double maxSupport, int enginePowerMax, int enginePowerNominal, int engineTorque, Long bikeModelId) {
        if (bikeModelId != null) {
            Bike bike = new Bike(frameNumber, this.customerRepository.findById(bikeOwnerID).orElseThrow(), bikeSize, milleage, accCapacity, productionDate, bikeModelRepository.findById(bikeModelId).orElseThrow());
            bikeRepository.save(bike);
            return bike;
        }
        Bike bike = new Bike(frameNumber, this.customerRepository.findById(bikeOwnerID).orElseThrow(), type, brand, productionDate, bikeSize, milleage, gearType, engineType, powertrain, accCapacity, maxSupport, enginePowerMax, enginePowerNominal, engineTorque);
        bikeModelRepository.save(bike.getBikeModel());
        bikeRepository.save(bike);
        return bike;
    }

    @Override
    public void delete(String frameNumber) {
        bikeRepository.delete(getByFrameNumber(frameNumber).orElseThrow(() -> new UsernameNotFoundException("Cannot delete non-existing bike")));
    }

    @Override
    public List<BikeModel> getAllBikeModels() {
        return bikeModelRepository.findAll();
    }


}
