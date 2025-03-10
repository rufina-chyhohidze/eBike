package be.kdg.integration4.domain;

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
//@ToString
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
        this.rolTroque = rolTroque;
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

    @Override
    public String toString() {
        return "TestLine{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", batteryVoltage=" + batteryVoltage +
                ", batteryCurrent=" + batteryCurrent +
                ", batteryCapacity=" + batteryCapacity +
                ", batteryTemperature=" + batteryTemperature +
                ", chargeStatus=" + chargeStatus +
                ", assistanceLevel=" + assistanceLevel +
                ", torqueCrank=" + torqueCrank +
                ", bikeWheelSpeed=" + bikeWheelSpeed +
                ", cadence=" + cadence +
                ", engineRPM=" + engineRPM +
                ", enginePower=" + enginePower +
                ", wheelPower=" + wheelPower +
                ", rolTroque=" + rolTroque +
                ", loadCell=" + loadCell +
                ", rol=" + rol +
                ", horizontalInclinationSensor=" + horizontalInclinationSensor +
                ", verticalInclinationSensor=" + verticalInclinationSensor +
                ", loadPower=" + loadPower +
                ", statusPlug=" + statusPlug +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Float getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(Float batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public Float getBatteryCurrent() {
        return batteryCurrent;
    }

    public void setBatteryCurrent(Float batteryCurrent) {
        this.batteryCurrent = batteryCurrent;
    }

    public Float getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(Float batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public Float getBatteryTemperature() {
        return batteryTemperature;
    }

    public void setBatteryTemperature(Float batteryTemperature) {
        this.batteryTemperature = batteryTemperature;
    }

    public Integer getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(Integer chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public Integer getAssistanceLevel() {
        return assistanceLevel;
    }

    public void setAssistanceLevel(Integer assistanceLevel) {
        this.assistanceLevel = assistanceLevel;
    }

    public Float getTorqueCrank() {
        return torqueCrank;
    }

    public void setTorqueCrank(Float torqueCrank) {
        this.torqueCrank = torqueCrank;
    }

    public Float getBikeWheelSpeed() {
        return bikeWheelSpeed;
    }

    public void setBikeWheelSpeed(Float bikeWheelSpeed) {
        this.bikeWheelSpeed = bikeWheelSpeed;
    }

    public Integer getCadence() {
        return cadence;
    }

    public void setCadence(Integer cadence) {
        this.cadence = cadence;
    }

    public Integer getEngineRPM() {
        return engineRPM;
    }

    public void setEngineRPM(Integer engineRPM) {
        this.engineRPM = engineRPM;
    }

    public Float getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(Float enginePower) {
        this.enginePower = enginePower;
    }

    public Float getWheelPower() {
        return wheelPower;
    }

    public void setWheelPower(Float wheelPower) {
        this.wheelPower = wheelPower;
    }

    public Float getRolTroque() {
        return rolTroque;
    }

    public void setRolTroque(Float rolTroque) {
        this.rolTroque = rolTroque;
    }

    public Float getLoadCell() {
        return loadCell;
    }

    public void setLoadCell(Float loadCell) {
        this.loadCell = loadCell;
    }

    public Float getRol() {
        return rol;
    }

    public void setRol(Float rol) {
        this.rol = rol;
    }

    public Float getHorizontalInclinationSensor() {
        return horizontalInclinationSensor;
    }

    public void setHorizontalInclinationSensor(Float horizontalInclinationSensor) {
        this.horizontalInclinationSensor = horizontalInclinationSensor;
    }

    public Float getVerticalInclinationSensor() {
        return verticalInclinationSensor;
    }

    public void setVerticalInclinationSensor(Float verticalInclinationSensor) {
        this.verticalInclinationSensor = verticalInclinationSensor;
    }

    public Integer getLoadPower() {
        return loadPower;
    }

    public void setLoadPower(Integer loadPower) {
        this.loadPower = loadPower;
    }

    public Boolean getStatusPlug() {
        return statusPlug;
    }

    public void setStatusPlug(Boolean statusPlug) {
        this.statusPlug = statusPlug;
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
