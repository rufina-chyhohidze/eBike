package be.kdg.integration4.service.dtos;

public class OverviewTestDTO {
    private Double maxMotorPower;     // Motor vermogen (max)
    private Double motorPowerDeviation; // Deviation from promised motor power
    private Double maxTorque;         // Motor koppel (max)
    private Double torqueDeviation;   // Deviation from promised torque
    private Double maxSupport;        // Max ondersteuning (calculated)
    private Double supportDeviation;  // Deviation from promised support
    private Double maxWheelPower;     // Wielvermogen (max)
    private Double wheelPowerDeviation; // Deviation from promised wheel power
    private Double score;             // Score (calculated)

    // Units
    private String maxMotorPowerUnit = "W";     // Unit for max motor power (Watts)
    private String motorPowerDeviationUnit = "%"; // Unit for motor power deviation (Percentage)
    private String maxTorqueUnit = "Nm";         // Unit for max torque (Newton meters)
    private String torqueDeviationUnit = "%";   // Unit for torque deviation (Percentage)
    private String maxSupportUnit = "%";        // Unit for max support (Percentage)
    private String supportDeviationUnit = "%";  // Unit for support deviation (Percentage)
    private String maxWheelPowerUnit = "W";     // Unit for max wheel power (Watts)
    private String wheelPowerDeviationUnit = "%"; // Unit for wheel power deviation (Percentage)
    private String scoreUnit = "%";             // Unit for score (Percentage)

    // Constructors
    public OverviewTestDTO(
            Double maxMotorPower,
            Double motorPowerDeviation,
            Double maxTorque,
            Double torqueDeviation,
            Double maxSupport,
            Double supportDeviation,
            Double maxWheelPower,
            Double wheelPowerDeviation,
            Double score
    ) {
        this.maxMotorPower = maxMotorPower;
        this.motorPowerDeviation = motorPowerDeviation;
        this.maxTorque = maxTorque;
        this.torqueDeviation = torqueDeviation;
        this.maxSupport = maxSupport;
        this.supportDeviation = supportDeviation;
        this.maxWheelPower = maxWheelPower;
        this.wheelPowerDeviation = wheelPowerDeviation;
        this.score = score;
    }

    // Getters and setters for the values
    public Double getMaxMotorPower() {
        return maxMotorPower;
    }

    public void setMaxMotorPower(Double maxMotorPower) {
        this.maxMotorPower = maxMotorPower;
    }

    public Double getMotorPowerDeviation() {
        return motorPowerDeviation;
    }

    public void setMotorPowerDeviation(Double motorPowerDeviation) {
        this.motorPowerDeviation = motorPowerDeviation;
    }

    public Double getMaxTorque() {
        return maxTorque;
    }

    public void setMaxTorque(Double maxTorque) {
        this.maxTorque = maxTorque;
    }

    public Double getTorqueDeviation() {
        return torqueDeviation;
    }

    public void setTorqueDeviation(Double torqueDeviation) {
        this.torqueDeviation = torqueDeviation;
    }

    public Double getMaxSupport() {
        return maxSupport;
    }

    public void setMaxSupport(Double maxSupport) {
        this.maxSupport = maxSupport;
    }

    public Double getSupportDeviation() {
        return supportDeviation;
    }

    public void setSupportDeviation(Double supportDeviation) {
        this.supportDeviation = supportDeviation;
    }

    public Double getMaxWheelPower() {
        return maxWheelPower;
    }

    public void setMaxWheelPower(Double maxWheelPower) {
        this.maxWheelPower = maxWheelPower;
    }

    public Double getWheelPowerDeviation() {
        return wheelPowerDeviation;
    }

    public void setWheelPowerDeviation(Double wheelPowerDeviation) {
        this.wheelPowerDeviation = wheelPowerDeviation;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    // Getters for the units
    public String getMaxMotorPowerUnit() {
        return maxMotorPowerUnit;
    }

    public String getMotorPowerDeviationUnit() {
        return motorPowerDeviationUnit;
    }

    public String getMaxTorqueUnit() {
        return maxTorqueUnit;
    }

    public String getTorqueDeviationUnit() {
        return torqueDeviationUnit;
    }

    public String getMaxSupportUnit() {
        return maxSupportUnit;
    }

    public String getSupportDeviationUnit() {
        return supportDeviationUnit;
    }

    public String getMaxWheelPowerUnit() {
        return maxWheelPowerUnit;
    }

    public String getWheelPowerDeviationUnit() {
        return wheelPowerDeviationUnit;
    }

    public String getScoreUnit() {
        return scoreUnit;
    }
}
