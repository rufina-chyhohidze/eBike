package be.kdg.integration4.service;

import be.kdg.integration4.domain.Bike;
import be.kdg.integration4.domain.BikeSize;
import be.kdg.integration4.repository.BikeRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BikeServiceImpl implements BikeService{
    BikeRepository bikeRepository;

    public BikeServiceImpl(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    @Override
    public Optional<Bike> findByFrameNumber(String frameNumber) {
         return bikeRepository.findBikeByFrameNumber(frameNumber);
    }

    @Override
    public List<Bike> findAll() {
        return bikeRepository.findAll();
    }

    @Override
    public void save(String frameNumber, String type, String brand, LocalDate registrationDate, LocalDate productionDate, BikeSize bikeSize, int milleage, String gearType, String engineType, String powertrain, int accCapacity, double maxSupport, int enginePowerMax, int enginePowerNominal, int engineTorque) {
        Bike bike = new Bike(frameNumber, type, brand, registrationDate, productionDate, bikeSize, milleage, gearType, engineType, powertrain, accCapacity, maxSupport, enginePowerMax, enginePowerNominal, engineTorque);
        bikeRepository.save(bike);
    }

    @Override
    public void delete(String frameNumber) {
        bikeRepository.delete(findByFrameNumber(frameNumber).orElseThrow(() -> new UsernameNotFoundException("Cannot delete non-existing bike")));
    }


}
