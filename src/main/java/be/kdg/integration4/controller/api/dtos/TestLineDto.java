package be.kdg.integration4.controller.api.dtos;

import java.time.LocalDateTime;

public record TestLineDto(
        Long id,
        LocalDateTime dateTime,
        Float batteryVoltage,
        Float batteryCurrent,
        Float batteryCapacity,
        Float batteryTemperature,
        Integer chargeStatus,
        Integer assistanceLevel,
        Float torqueCrank,
        Float bikeWheelSpeed,
        Integer cadence,
        Integer engineRPM,
        Float enginePower,
        Float wheelPower,
        Float rolTroque,
        Float loadCell,
        Float rol,
        Float horizontalInclinationSensor,
        Float verticalInclinationSensor,
        Integer loadPower,
        Boolean statusPlug

) {
}
