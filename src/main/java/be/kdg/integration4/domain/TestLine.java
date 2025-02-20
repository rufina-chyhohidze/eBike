package be.kdg.integration4.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class TestLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime dateTime;
    private Float batteryVoltage;
    private Float batteryCurrent;
    private Float batteryCapacity;
    private Float batteryTemperature;
    private Integer chargeStatus = 0;
    private Integer assistanceLevel;
    private Float torqueCrank;
    private Float bikeWheelSpeed;
    private Integer cadence;
    private Integer engineRPM;
    private Float enginePower;
    private Float wheelPower;
    private Float rolTroque;
    private Float loadCell;
    private Float rol;
    private Float horizontalInclinationSensor;
    private Float verticalInclinationSensor;
    private Integer loadPower;
    private Boolean statusPlug;



    public TestLine(LocalDate dateTime, int batteryVoltage) {
        this.dateTime = dateTime;
        this.batteryVoltage = batteryVoltage;
    }
}
