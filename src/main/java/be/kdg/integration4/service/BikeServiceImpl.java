package be.kdg.integration4.service;

import be.kdg.integration4.domain.Bike;
import be.kdg.integration4.domain.BikeSize;
import be.kdg.integration4.repository.BikeRepository;

import java.time.LocalDate;
import java.util.List;

public class BikeServiceImpl implements BikeService{
    BikeRepository bikeRepository;
    @Override
    public Bike findByFrameNumber(String frameNumber) {
         return bikeRepository.findBikeByFrameNumber(frameNumber).orElse(null);
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
        bikeRepository.delete(findByFrameNumber(frameNumber));
    }


}
