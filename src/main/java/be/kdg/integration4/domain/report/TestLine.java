package be.kdg.integration4.domain.report;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NoArgsConstructor
@ToString
public class TestLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    private Float rolTorque;
    private Float loadCell;
    private Float rol;
    private Float horizontalInclinationSensor;
    private Float verticalInclinationSensor;
    private Integer loadPower;
    private Boolean statusPlug;

    // Constructor
    public TestLine(LocalDateTime dateTime, Float batteryVoltage, Float batteryCurrent, Float batteryCapacity, Float batteryTemperature, Integer chargeStatus, Integer assistanceLevel, Float torqueCrank, Float bikeWheelSpeed, Integer cadence, Integer engineRPM, Float enginePower, Float wheelPower, Float rolTroque, Float loadCell, Float rol, Float horizontalInclinationSensor, Float verticalInclinationSensor, Integer loadPower, Boolean statusPlug) {
        this.dateTime = dateTime;
        this.batteryVoltage = batteryVoltage;
        this.batteryCurrent = batteryCurrent;
        this.batteryCapacity = batteryCapacity;
        this.batteryTemperature = batteryTemperature;
        this.chargeStatus = chargeStatus;
        this.assistanceLevel = assistanceLevel;
        this.torqueCrank = torqueCrank;
        this.bikeWheelSpeed = bikeWheelSpeed;
        this.cadence = cadence;
        this.engineRPM = engineRPM;
        this.enginePower = enginePower;
        this.wheelPower = wheelPower;
        this.rolTorque = rolTroque;
        this.loadCell = loadCell;
        this.rol = rol;
        this.horizontalInclinationSensor = horizontalInclinationSensor;
        this.verticalInclinationSensor = verticalInclinationSensor;
        this.loadPower = loadPower;
        this.statusPlug = statusPlug;
    }

    // Method to parse the date-time format
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS");
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    // Method to convert the status plug value from "TRUE"/"FALSE" to boolean
    public static Boolean parseStatusPlug(String statusPlugStr) {
        return "TRUE".equalsIgnoreCase(statusPlugStr);
    }
}




//package be.kdg.integration4.domain;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import lombok.Data;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Entity
//@Data
//@NoArgsConstructor
//public class TestLine {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    private LocalDateTime dateTime;
//    private Float batteryVoltage;
//    private Float batteryCurrent;
//    private Float batteryCapacity;
//    private Float batteryTemperature;
//    private Integer chargeStatus = 0;
//    private Integer assistanceLevel;
//    private Float torqueCrank;
//    private Float bikeWheelSpeed;
//    private Integer cadence;
//    private Integer engineRPM;
//    private Float enginePower;
//    private Float wheelPower;
//    private Float rolTroque;
//    private Float loadCell;
//    private Float rol;
//    private Float horizontalInclinationSensor;
//    private Float verticalInclinationSensor;
//    private Integer loadPower;
//    private Boolean statusPlug;
//
//
//
//    public TestLine(LocalDateTime dateTime, Float batteryVoltage, Float batteryCurrent, Float batteryCapacity, Float batteryTemperature, Integer chargeStatus, Integer assistanceLevel, Float torqueCrank, Float bikeWheelSpeed, Integer cadence, Integer engineRPM, Float enginePower, Float wheelPower, Float rolTroque, Float loadCell, Float rol, Float horizontalInclinationSensor, Float verticalInclinationSensor, Integer loadPower, Boolean statusPlug) {
//        this.dateTime = dateTime;
//        this.batteryVoltage = batteryVoltage;
//        this.batteryCurrent = batteryCurrent;
//        this.batteryCapacity = batteryCapacity;
//        this.batteryTemperature = batteryTemperature;
//        this.chargeStatus = chargeStatus;
//        this.assistanceLevel = assistanceLevel;
//        this.torqueCrank = torqueCrank;
//        this.bikeWheelSpeed = bikeWheelSpeed;
//        this.cadence = cadence;
//        this.engineRPM = engineRPM;
//        this.enginePower = enginePower;
//        this.wheelPower = wheelPower;
//        this.rolTroque = rolTroque;
//        this.loadCell = loadCell;
//        this.rol = rol;
//        this.horizontalInclinationSensor = horizontalInclinationSensor;
//        this.verticalInclinationSensor = verticalInclinationSensor;
//        this.loadPower = loadPower;
//        this.statusPlug = statusPlug;
//    }
//}
