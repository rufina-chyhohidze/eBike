package be.kdg.integration4.controller.api.dtos.mappers;

import be.kdg.integration4.controller.api.dtos.BikeDto;
import be.kdg.integration4.domain.enums.BikeSize;
import be.kdg.integration4.domain.profile.Customer;
import be.kdg.integration4.domain.report.Bike;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class BikeDtoMapper {

    public List<BikeDto> toBikeDtoList(List<Bike> bikeList) {
        if ( bikeList == null ) {
            return null;
        }

        List<BikeDto> list = new ArrayList<BikeDto>( bikeList.size() );
        for ( Bike bike : bikeList ) {
            list.add( toBikeDto( bike ) );
        }

        return list;
    }

    public BikeDto toBikeDto(Bike bike) {
        if ( bike == null ) {
            return null;
        }

        Long bikeOwnerId = null;
        String frameNumber = null;
        String type = null;
        String brand = null;
        LocalDate registrationDate = null;
        LocalDate productionDate = null;
        BikeSize bikeSize = null;
        Integer milleage = null;
        String gearType = null;
        String engineType = null;
        String powertrain = null;
        Integer accCapacity = null;
        Integer maxSupport = null;
        Integer enginePowerMax = null;
        Integer enginePowerNominal = null;
        Integer engineTorque = null;

        bikeOwnerId = bikeBikeOwnerId( bike );
        frameNumber = bike.getFrameNumber();
        type = bike.getBikeModel().getType();
        brand = bike.getBikeModel().getBrand();
        if ( bike.getRegistrationDate() != null ) {
            registrationDate = bike.getRegistrationDate().toLocalDate();
        }
        productionDate = bike.getProductionDate();
        bikeSize = bike.getBikeSize();
        milleage = bike.getMilleage();
        gearType = bike.getBikeModel().getGearType();
        engineType = bike.getBikeModel().getEngineType();
        powertrain = bike.getBikeModel().getPowertrain();
        accCapacity = bike.getAccCapacity();
        maxSupport = (int) bike.getBikeModel().getMaxSupport();
        enginePowerMax = bike.getBikeModel().getEnginePowerMax();
        enginePowerNominal = bike.getBikeModel().getEnginePowerNominal();
        engineTorque = bike.getBikeModel().getEngineTorque();

        BikeDto bikeDto = new BikeDto( bikeOwnerId, frameNumber, type, brand,productionDate, bikeSize, milleage, gearType, engineType, powertrain, accCapacity, maxSupport, enginePowerMax, enginePowerNominal, engineTorque,null);

        return bikeDto;
    }

    private Long bikeBikeOwnerId(Bike bike) {
        Customer bikeOwner = bike.getBikeOwner();
        if ( bikeOwner == null ) {
            return null;
        }
        return bikeOwner.getId();
    }
}
