package be.kdg.integration4.service.implementations;

import be.kdg.integration4.domain.Bike;
import be.kdg.integration4.domain.BikeSize;
import be.kdg.integration4.repository.BikeRepository;
import be.kdg.integration4.service.interfaces.BikeService;
import be.kdg.integration4.service.interfaces.CustomerService;
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
    private final CustomerService customerService;

    public BikeServiceImpl(BikeRepository bikeRepository, CustomerService customerService) {
        this.bikeRepository = bikeRepository;
        this.customerService = customerService;
    }

    @Override
    public Optional<Bike> findByFrameNumber(String frameNumber) {
         return bikeRepository.findBikeByFrameNumber(frameNumber);
    }

    @Override
    public Set<Bike> getBikesByOwnerId(Long ownerId) {
        return this.bikeRepository.findBikesByBikeOwnerId(ownerId);
    }

    @Override
    public List<Bike> findAll() {
        return bikeRepository.findAll();
    }

    @Override
    public void save(String frameNumber, Long bikeOwnerID, String type, String brand, LocalDateTime registrationDate, LocalDate productionDate, BikeSize bikeSize, int milleage, String gearType, String engineType, String powertrain, int accCapacity, double maxSupport, int enginePowerMax, int enginePowerNominal, int engineTorque) {
        Bike bike = new Bike(frameNumber, this.customerService.findById(bikeOwnerID), type, brand, registrationDate, productionDate, bikeSize, milleage, gearType, engineType, powertrain, accCapacity, maxSupport, enginePowerMax, enginePowerNominal, engineTorque);
        bikeRepository.save(bike);
    }

    @Override
    public void delete(String frameNumber) {
        bikeRepository.delete(findByFrameNumber(frameNumber).orElseThrow(() -> new UsernameNotFoundException("Cannot delete non-existing bike")));
    }


}
